import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Layout } from "antd";
import Blogs from "./components/AllBlogs/AllBlogs";
import SignUp from "./components/Registration/SignUp";
import SignIn from "./components/Login/SignIn";
import UpdateUser from "./components/Component/UpdateUser";
import Navbar from "./components/Component/Navbar";
import Story from "./components/Component/SingleStory";
import CreateBlog from "./components/CreateBlog/CreateBlog";
import MyStories from "./components/MyStories/MyStories";
import TagStories from "./components/TagStories/TagStories";
import { authcontext } from "./components/Component/AuthContext";

const { Content, Footer } = Layout;

function App() {
  const [islogged, setLogStatus] = useState(localStorage.getItem("token") != null);

  return (
    <BrowserRouter>
      <authcontext.Provider value={{ islogged, setLogStatus }}>
        <Layout style={{ minHeight: "100vh" }}>
          <Navbar />
          <Content className="page">
            <Routes>
              <Route path="/" element={<Blogs />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/signin" element={<SignIn />} />
              <Route path="/blogs/new" element={<CreateBlog />} />
              <Route path="/my-stories" element={<MyStories />} />
              <Route path="/tag/:name" element={<TagStories />} />
              <Route path="/user" element={<UpdateUser />} />
              <Route path="/story/:storyId" element={<Story />} />
            </Routes>
          </Content>
          <Footer style={{ textAlign: "center" }}>DevDiary - made by Haseb</Footer>
        </Layout>
      </authcontext.Provider>
    </BrowserRouter>
  );
}

export default App;
