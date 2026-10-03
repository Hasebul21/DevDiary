import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Row, Col, Spin, Empty, Button, Typography, message } from "antd";
import Card from "../Component/Card";
import { BASE_URL, getToken } from "../../api";

const { Title } = Typography;

function MyStories() {
  const navigate = useNavigate();
  const [storyList, setStoryList] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    if (token == null) {
      navigate("/signin");
      return;
    }

    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/stories/my", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setStoryList(res.data);
      } catch (err) {
        console.log(err);
        message.error("Could not load your stories");
      }
      setLoading(false);
    };
    fetchData();
  }, [navigate]);

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  return (
    <div>
      <Title level={3}>My Stories</Title>
      {storyList.length === 0 ? (
        <Empty description="You have not written any story yet">
          <Button type="primary" onClick={() => navigate("/blogs/new")}>
            Write your first story
          </Button>
        </Empty>
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

export default MyStories;
