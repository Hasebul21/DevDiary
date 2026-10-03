import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { Card, List, Form, Input, Button, Popconfirm, Typography, message } from "antd";
import { BASE_URL, getToken, isAdmin } from "../../api";

const { Text } = Typography;

function Comments(props) {
  const storyId = props.storyId;
  const user = props.user;
  const [form] = Form.useForm();
  const [comments, setComments] = useState([]);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/stories/" + storyId + "/comments");
        setComments(res.data);
      } catch (err) {
        console.log(err);
      }
    };
    fetchData();
  }, [storyId]);

  const addComment = async (values) => {
    setSending(true);
    try {
      const res = await axios.post(BASE_URL + "/stories/" + storyId + "/comments", values, {
        headers: { Authorization: `Bearer ${getToken()}` },
      });
      setComments([...comments, res.data]);
      form.resetFields();
    } catch (err) {
      message.error("Could not add the comment");
    }
    setSending(false);
  };

  const deleteComment = async (id) => {
    try {
      await axios.delete(BASE_URL + "/comments/" + id, {
        headers: { Authorization: `Bearer ${getToken()}` },
      });
      setComments(comments.filter((c) => c.id !== id));
      message.success("Comment deleted");
    } catch (err) {
      message.error("Could not delete the comment");
    }
  };

  return (
    <Card title={"Comments (" + comments.length + ")"} className="story-box" style={{ marginTop: 20 }}>
      <List
        dataSource={comments}
        locale={{ emptyText: "No comments yet. Be the first one!" }}
        renderItem={(comment) => (
          <List.Item
            actions={
              user === comment.author || isAdmin()
                ? [
                    <Popconfirm
                      title="Delete this comment?"
                      onConfirm={() => deleteComment(comment.id)}
                      okText="Yes"
                      cancelText="No"
                    >
                      <Button type="link" danger size="small">
                        Delete
                      </Button>
                    </Popconfirm>,
                  ]
                : []
            }
          >
            <List.Item.Meta
              title={
                <span>
                  <Text strong>{comment.author}</Text>
                  <Text type="secondary" style={{ marginLeft: 10, fontWeight: "normal", fontSize: 12 }}>
                    {new Date(comment.createdDate).toLocaleString()}
                  </Text>
                </span>
              }
              description={
                <div style={{ whiteSpace: "pre-wrap", color: "rgba(0,0,0,0.88)" }}>{comment.text}</div>
              }
            />
          </List.Item>
        )}
      />

      {user ? (
        <Form form={form} onFinish={addComment} style={{ marginTop: 15 }}>
          <Form.Item
            name="text"
            rules={[{ required: true, whitespace: true, message: "Write something first" }]}
          >
            <Input.TextArea rows={3} maxLength={1000} placeholder="Write a comment..." />
          </Form.Item>
          <Button type="primary" htmlType="submit" loading={sending}>
            Add Comment
          </Button>
        </Form>
      ) : (
        <Text type="secondary">
          <Link to="/signin">Sign in</Link> to write a comment.
        </Text>
      )}
    </Card>
  );
}

export default Comments;
