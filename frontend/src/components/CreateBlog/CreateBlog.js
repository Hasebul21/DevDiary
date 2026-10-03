import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Card, Form, Input, Button, Select, message } from "antd";
import { BASE_URL, getToken } from "../../api";

function CreateBlog() {
  const navigate = useNavigate();
  const token = getToken();

  useEffect(() => {
    if (token == null) {
      navigate("/signin");
    }
  }, []);

  const postBlogHandler = async (values) => {
    try {
      await axios.post(BASE_URL + "/stories/", values, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success("Successfully created");
      navigate("/");
    } catch (err) {
      message.error("Unauthorized user. Please login first");
    }
  };

  return (
    <Card title="Write a new story" className="story-box">
      <Form layout="vertical" onFinish={postBlogHandler}>
        <Form.Item label="Title" name="title" rules={[{ required: true, message: "Please enter a title" }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Description" name="description" rules={[{ required: true, message: "Please write something" }]}>
          <Input.TextArea rows={8} />
        </Form.Item>
        <Form.Item label="Tags" name="tags">
          <Select mode="tags" placeholder="Type a tag and press enter, for example java" tokenSeparators={[",", " "]} />
        </Form.Item>
        <Button type="primary" htmlType="submit">
          Save
        </Button>
      </Form>
    </Card>
  );
}

export default CreateBlog;
