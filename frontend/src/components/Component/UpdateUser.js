import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Card, Form, Input, Button, Spin, Tag, message } from "antd";
import { BASE_URL, getToken } from "../../api";

function UpdateUser() {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [user, setUser] = useState(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const token = getToken();
    if (token == null) {
      navigate("/signin");
      return;
    }

    const fetchData = async () => {
      try {
        const res = await axios.get(BASE_URL + "/users/me", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setUser(res.data);
        form.setFieldsValue({
          email: res.data.email,
          name: res.data.name,
          phone: res.data.phone,
        });
      } catch (err) {
        message.error("Could not load your profile");
      }
    };
    fetchData();
  }, [form, navigate]);

  const updateHandler = async (values) => {
    setSaving(true);
    const body = {
      name: values.name,
      phone: values.phone,
      password: values.password ? values.password : null,
    };
    try {
      const res = await axios.put(BASE_URL + "/users/" + user.id, body, {
        headers: { Authorization: `Bearer ${getToken()}` },
      });
      setUser(res.data);
      form.setFieldsValue({ password: "", confirm: "" });
      message.success("Profile updated");
    } catch (err) {
      message.error(err.response ? err.response.data.message : "Something went wrong");
    }
    setSaving(false);
  };

  if (user == null) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  return (
    <Card
      title="Update Profile"
      className="form-box"
      extra={user.role === "ADMIN" ? <Tag color="red">Admin</Tag> : null}
    >
      <Form form={form} layout="vertical" onFinish={updateHandler}>
        <Form.Item label="Email" name="email" extra="Email can not be changed">
          <Input disabled />
        </Form.Item>
        <Form.Item
          label="Name"
          name="name"
          rules={[
            { required: true, message: "Please enter your name" },
            { pattern: /^[A-Za-z\s]+$/, message: "Name can have only letters and spaces" },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Phone"
          name="phone"
          rules={[
            { required: true, message: "Please enter your phone number" },
            { pattern: /^[0-9]{11}$/, message: "Phone number must have exactly 11 digits" },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="New Password"
          name="password"
          extra="Leave empty to keep your current password"
          rules={[
            {
              pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,}$/,
              message: "At least 8 characters with uppercase, lowercase and a number",
            },
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="Confirm New Password"
          name="confirm"
          dependencies={["password"]}
          rules={[
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!getFieldValue("password") || getFieldValue("password") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error("Passwords do not match"));
              },
            }),
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Button type="primary" htmlType="submit" block loading={saving}>
          Save
        </Button>
      </Form>
    </Card>
  );
}

export default UpdateUser;
