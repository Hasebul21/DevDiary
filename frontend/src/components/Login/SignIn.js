import React, { useContext, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { Card, Form, Input, Button, Divider, message } from "antd";
import { authcontext } from "../Component/AuthContext";
import { BASE_URL } from "../../api";

function SignIn() {
  const navigate = useNavigate();
  const { setLogStatus } = useContext(authcontext);

  useEffect(() => {
    if (localStorage.getItem("token") != null) {
      navigate("/");
    }
  }, []);

  const signIn = async (url, values) => {
    try {
      const res = await axios.post(BASE_URL + url, values);
      localStorage.setItem("token", res.data);
      setLogStatus(true);
      navigate("/");
    } catch (err) {
      message.error(err.response ? err.response.data.message : "Login failed");
    }
  };

  const submitHandler = (values) => signIn("/signin", values);

  const guestHandler = () => signIn("/signin/guest");

  return (
    <Card title="Welcome back" className="form-box">
      <Form layout="vertical" onFinish={submitHandler}>
        <Form.Item
          label="Email"
          name="email"
          rules={[{ required: true, type: "email", message: "Please enter a valid email" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Password"
          name="password"
          rules={[{ required: true, message: "Please enter your password" }]}
        >
          <Input.Password />
        </Form.Item>
        <Button type="primary" htmlType="submit" block>
          Sign In
        </Button>
      </Form>
      <Divider plain>or</Divider>
      <Button block onClick={guestHandler}>
        Continue as Guest
      </Button>
      <p style={{ marginTop: 15 }}>
        Don't have an account? <Link to="/signup">Sign up</Link>
      </p>
    </Card>
  );
}

export default SignIn;
