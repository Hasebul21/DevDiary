import React from "react";
import { useNavigate } from "react-router-dom";
import { Card, Button, Typography } from "antd";

const { Paragraph, Text } = Typography;

function StoryCard(props) {
  const navigate = useNavigate();
  const data = props.story;

  return (
    <Card
      title={data.title}
      actions={[
        <Button type="link" onClick={() => navigate(`/story/${data.id}`)}>
          View Details
        </Button>,
      ]}
    >
      <Text type="secondary">By {data.author}</Text>
      <br />
      <Text type="secondary">{data.createdDate}</Text>
      <Paragraph ellipsis={{ rows: 3 }} style={{ marginTop: 10 }}>
        {data.description}
      </Paragraph>
    </Card>
  );
}

export default StoryCard;
