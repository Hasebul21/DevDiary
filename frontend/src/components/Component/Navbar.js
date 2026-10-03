import React, { useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Layout, Menu, Tag } from "antd";
import { authcontext } from "./AuthContext";
import { isAdmin } from "../../api";

const { Header } = Layout;

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { islogged, setLogStatus } = useContext(authcontext);

  let items = [{ key: "/", label: "Home" }];

  if (islogged) {
    items.push({ key: "/blogs/new", label: "Create Blog" });
    items.push({ key: "/my-stories", label: "My Stories" });
    items.push({ key: "/user", label: "Update Profile" });
    items.push({ key: "logout", label: "Logout" });
  } else {
    items.push({ key: "/signin", label: "Sign In" });
    items.push({ key: "/signup", label: "Sign Up" });
  }

  const onMenuClick = (e) => {
    if (e.key === "logout") {
      localStorage.clear();
      setLogStatus(false);
      navigate("/");
    } else {
      navigate(e.key);
    }
  };

  return (
    <Header style={{ display: "flex", alignItems: "center" }}>
      <div className="logo">TECH WORLD WITH HASEB</div>
      {islogged && isAdmin() && <Tag color="red">Admin</Tag>}
      <Menu
        theme="dark"
        mode="horizontal"
        selectedKeys={[location.pathname]}
        items={items}
        onClick={onMenuClick}
        style={{ flex: 1, minWidth: 0 }}
      />
    </Header>
  );
}
