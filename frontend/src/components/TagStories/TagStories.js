import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Row, Col, Spin, Empty, Typography, Tag, Button, message } from "antd";
import Card from "../Component/Card";
import { BASE_URL } from "../../api";

const { Title } = Typography;

function TagStories() {
  const { name } = useParams();
  const navigate = useNavigate();
  const [storyList, setStoryList] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await axios.get(BASE_URL + "/tags/" + encodeURIComponent(name) + "/stories");
        setStoryList(res.data);
      } catch (err) {
        console.log(err);
        message.error("Could not load the stories");
      }
      setLoading(false);
    };
    fetchData();
  }, [name]);

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  return (
    <div>
      <Title level={3}>
        Stories tagged{" "}
        <Tag color="blue" style={{ fontSize: 18, padding: "4px 10px" }}>
          #{name}
        </Tag>
      </Title>
      <Button style={{ marginBottom: 20 }} onClick={() => navigate("/")}>
        Back to all stories
      </Button>
      {storyList.length === 0 ? (
        <Empty description="No story with this tag" />
      ) : (
        <Row gutter={[16, 16]}>
          {storyList.map((story) => (
            <Col xs={24} sm={12} lg={8} key={story.id}>
              <Card story={story} />
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
}

export default TagStories;
