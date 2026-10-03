import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import jwt_decode from "jwt-decode";
import { Card, Form, Input, Button, Popconfirm, Space, Typography, Spin, Select, message } from "antd";
import { BASE_URL, getToken } from "../../api";

const { Text } = Typography;

export default function Story() {
  const { storyId } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [story, setStory] = useState(null);
  const [user, setUser] = useState(null);

  const token = getToken();

  useEffect(() => {
    if (token == null) {
      navigate("/signin");
      return;
    }
    setUser(jwt_decode(token).sub);

    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/stories/" + storyId);
        setStory(res.data);
        form.setFieldsValue({
          title: res.data.title,
          description: res.data.description,
          tags: res.data.tags,
        });
      } catch (err) {
        message.error("Story not found");
        navigate("/");
      }
    };
    fetchData();
  }, [storyId]);

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

  if (story == null) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  const isMine = user === story.author;

  return (
    <Card className="story-box">
      <Text strong>Author: </Text> {story.author}
      <br />
      <Text strong>Created: </Text> {story.createdDate}

      <Form form={form} layout="vertical" onFinish={updateHandler} disabled={!isMine} style={{ marginTop: 20 }}>
        <Form.Item label="Title" name="title" rules={[{ required: true, message: "Title can't be empty" }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Description" name="description" rules={[{ required: true, message: "Description can't be empty" }]}>
          <Input.TextArea rows={8} />
        </Form.Item>
        <Form.Item label="Tags" name="tags">
          <Select mode="tags" placeholder="Type a tag and press enter, for example java" tokenSeparators={[",", " "]} />
        </Form.Item>

        {isMine && (
          <Space>
            <Button type="primary" htmlType="submit">
              Save
            </Button>
            <Popconfirm title="Delete this story?" onConfirm={deleteHandler} okText="Yes" cancelText="No">
              <Button danger>Delete</Button>
            </Popconfirm>
          </Space>
        )}
      </Form>
    </Card>
  );
}
