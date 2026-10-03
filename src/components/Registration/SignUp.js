import React, { useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { Card, Form, Input, Button, message } from "antd";
import { authcontext } from "../Component/AuthContext";
import { BASE_URL } from "../../api";

function SignUp() {
  const { setLogStatus } = useContext(authcontext);
  const navigate = useNavigate();

  const submitHandler = async (values) => {
    try {
      const res = await axios.post(BASE_URL + "/signup", values);
      message.success("Successfully registered");
      localStorage.setItem("token", res.data);
      setLogStatus(true);
      navigate("/");
    } catch (err) {
      message.error(err.response ? err.response.data.message : "Registration failed");
    }
  };

  return (
    <Card title="Be a member and share your experience" className="form-box">
      <Form layout="vertical" onFinish={submitHandler}>
        <Form.Item
          label="Email"
          name="email"
          rules={[{ required: true, type: "email", message: "Please enter a valid email" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item label="Name" name="name" rules={[{ required: true, message: "Please enter your name" }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Phone" name="phone" rules={[{ required: true, message: "Please enter your phone number" }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Password" name="password" rules={[{ required: true, message: "Please enter a password" }]}>
          <Input.Password />
        </Form.Item>
        <Button type="primary" htmlType="submit" block>
          Register
        </Button>
      </Form>
      <p style={{ marginTop: 15 }}>
        Already have an account? <Link to="/signin">Sign in</Link>
      </p>
    </Card>
  );
}

export default SignUp;
