import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import jwt_decode from "jwt-decode";
import { Card, Form, Input, Button, Popconfirm, Space, Typography, Spin, Select, Tag, message } from "antd";
import Comments from "./Comments";
import { BASE_URL, getToken } from "../../api";

const { Text, Title, Paragraph } = Typography;

export default function Story() {
  const { storyId } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [story, setStory] = useState(null);
  const [likes, setLikes] = useState({ count: 0, users: [] });

  const token = getToken();
  // email of the logged in user, null if not logged in
  const user = token ? jwt_decode(token).sub : null;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/stories/" + storyId);
        setStory(res.data);
        form.setFieldsValue({
          title: res.data.title,
          description: res.data.description,
          tags: res.data.tags,
        });
        const likeRes = await axios.get(BASE_URL + "/stories/" + storyId + "/likes");
        setLikes(likeRes.data);
      } catch (err) {
        message.error("Story not found");
        navigate("/");
      }
    };
    fetchData();
  }, [storyId, form, navigate]);

  const updateHandler = async (values) => {
    try {
      await axios.put(BASE_URL + "/stories/" + storyId, values, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success("Successfully updated");
      navigate("/");
    } catch (err) {
      message.error(err.response ? err.response.data.message : "Something went wrong");
    }
  };

  const deleteHandler = async () => {
    try {
      await axios.delete(BASE_URL + "/stories/" + storyId, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success("Successfully deleted");
      navigate("/");
    } catch (err) {
      message.error(err.response ? err.response.data.message : "Something went wrong");
    }
  };

  const likeHandler = async () => {
    if (user == null) {
      message.info("Please sign in to like a story");
      navigate("/signin");
      return;
    }
    try {
      const res = await axios.post(BASE_URL + "/stories/" + storyId + "/likes", null, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setLikes(res.data);
    } catch (err) {
      message.error("Something went wrong");
    }
  };

  if (story == null) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  const isMine = user === story.author;
  const likedByMe = user != null && likes.users.includes(user);

  return (
    <div>
      <Card className="story-box">
        <Text strong>Author: </Text> {story.author}
        <br />
        <Text strong>Created: </Text> {new Date(story.createdDate).toLocaleString()}
        <br />
        <Button
          type={likedByMe ? "primary" : "default"}
          onClick={likeHandler}
          style={{ marginTop: 15 }}
          title={likes.users.join(", ")}
        >
          {likedByMe ? "♥ Liked" : "♡ Like"} ({likes.count})
        </Button>

        {isMine ? (
          <Form form={form} layout="vertical" onFinish={updateHandler} style={{ marginTop: 20 }}>
            <Form.Item label="Title" name="title" rules={[{ required: true, message: "Title can't be empty" }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Description" name="description" rules={[{ required: true, message: "Description can't be empty" }]}>
              <Input.TextArea rows={8} />
            </Form.Item>
            <Form.Item label="Tags" name="tags">
              <Select mode="tags" placeholder="Type a tag and press enter, for example java" tokenSeparators={[",", " "]} />
            </Form.Item>
            <Space>
              <Button type="primary" htmlType="submit">
                Save
              </Button>
              <Popconfirm title="Delete this story?" onConfirm={deleteHandler} okText="Yes" cancelText="No">
                <Button danger>Delete</Button>
              </Popconfirm>
            </Space>
          </Form>
        ) : (
          <div style={{ marginTop: 20 }}>
            <Title level={3}>{story.title}</Title>
            <Paragraph style={{ whiteSpace: "pre-wrap" }}>{story.description}</Paragraph>
            {story.tags.map((tag) => (
              <Tag key={tag} color="blue" style={{ cursor: "pointer" }} onClick={() => navigate(`/tag/${tag}`)}>
                #{tag}
              </Tag>
            ))}
          </div>
        )}
      </Card>

      <Comments storyId={storyId} user={user} />
    </div>
  );
}
