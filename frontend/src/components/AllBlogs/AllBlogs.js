import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Row, Col, Spin, Empty, Pagination, Input, Tag, message } from "antd";
import Card from "../Component/Card";
import { BASE_URL } from "../../api";

const PAGE_SIZE = 6;

function Blogs() {
  const [storyList, setStoryList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [keyword, setKeyword] = useState("");
  const [tags, setTags] = useState([]);
  const navigate = useNavigate();

  // load all tags one time
  useEffect(() => {
    axios
      .get(BASE_URL + "/tags/")
      .then((res) => setTags(res.data))
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        if (keyword !== "") {
          // search mode, show all results without pagination
          const res = await axios.get(BASE_URL + "/stories/search", {
            params: { keyword: keyword },
          });
          setStoryList(res.data);
        } else {
          // backend page number starts from 0
          const res = await axios.get(BASE_URL + "/stories/page", {
            params: { pageNo: page - 1, pageSize: PAGE_SIZE },
          });
          setStoryList(res.data.stories);
          setTotal(res.data.totalElements);
        }
      } catch (err) {
        console.log(err);
        message.error("Could not load the stories");
      }
      setLoading(false);
    };
    fetchData();
  }, [page, keyword]);

  const searchHandler = (value) => {
    setKeyword(value.trim());
    setPage(1);
  };

  let content;
  if (loading) {
    content = <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  } else if (storyList.length === 0) {
    content = <Empty description={keyword !== "" ? "No story found for \"" + keyword + "\"" : "No stories yet"} />;
  } else {
    content = (
      <Row gutter={[16, 16]}>
        {storyList.map((story) => (
          <Col xs={24} sm={12} lg={8} key={story.id}>
            <Card story={story} />
          </Col>
        ))}
      </Row>
    );
  }

  return (
    <div>
      <Input.Search
        placeholder="Search stories by title or description"
        allowClear
        enterButton="Search"
        size="large"
        onSearch={searchHandler}
        style={{ maxWidth: 600, margin: "0 auto 30px", display: "flex" }}
      />
      {tags.length > 0 && (
        <div style={{ textAlign: "center", marginBottom: 25 }}>
          {tags.map((tag) => (
            <Tag key={tag} color="blue" style={{ cursor: "pointer", marginBottom: 8 }} onClick={() => navigate(`/tag/${tag}`)}>
              #{tag}
            </Tag>
          ))}
        </div>
      )}
      {content}
      {!loading && keyword === "" && total > PAGE_SIZE && (
        <Pagination
          style={{ marginTop: 30, justifyContent: "center" }}
          current={page}
          pageSize={PAGE_SIZE}
          total={total}
          onChange={(p) => {
            setPage(p);
            window.scrollTo(0, 0);
          }}
          showSizeChanger={false}
        />
      )}
    </div>
  );
}

export default Blogs;
