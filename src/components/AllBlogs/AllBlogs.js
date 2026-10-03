import React, { useEffect, useState } from "react";
import axios from "axios";
import { Row, Col, Spin, Empty, message } from "antd";
import Card from "../Component/Card";
import { BASE_URL } from "../../api";

function Blogs() {
  const [storyList, setStoryList] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/stories/");
        setStoryList(res.data);
      } catch (err) {
        console.log(err);
        message.error("Could not load the stories");
      }
      setLoading(false);
    };
    fetchData();
  }, []);

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  if (storyList.length === 0) {
    return <Empty description="No stories yet" />;
  }

  return (
    <Row gutter={[16, 16]}>
      {storyList.map((story) => (
        <Col xs={24} sm={12} lg={8} key={story.id}>
          <Card story={story} />
        </Col>
      ))}
    </Row>
  );
}

export default Blogs;
