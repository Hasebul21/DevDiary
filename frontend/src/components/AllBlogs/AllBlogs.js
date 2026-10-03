import React, { useEffect, useState } from "react";
import axios from "axios";
import { Row, Col, Spin, Empty, Pagination, message } from "antd";
import Card from "../Component/Card";
import { BASE_URL } from "../../api";

const PAGE_SIZE = 6;

function Blogs() {
  const [storyList, setStoryList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // backend page number starts from 0
        const res = await axios.get(BASE_URL + "/stories/page", {
          params: { pageNo: page - 1, pageSize: PAGE_SIZE },
        });
        setStoryList(res.data.stories);
        setTotal(res.data.totalElements);
      } catch (err) {
        console.log(err);
        message.error("Could not load the stories");
      }
      setLoading(false);
    };
    fetchData();
  }, [page]);

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: 100 }} />;
  }

  if (storyList.length === 0) {
    return <Empty description="No stories yet" />;
  }

  return (
    <div>
      <Row gutter={[16, 16]}>
        {storyList.map((story) => (
          <Col xs={24} sm={12} lg={8} key={story.id}>
            <Card story={story} />
          </Col>
        ))}
      </Row>
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
    </div>
  );
}

export default Blogs;
