import React from 'react';
import DocumentTitle from 'react-document-title';
import Layout from './Layout';

export default (props) => {
  return (
    <DocumentTitle title="Not Found | 创意信息-在线帮助文档">
      <Layout {...props}>
        <h1 className="entry-title">404 Not Found!</h1>
      </Layout>
    </DocumentTitle>
  );
}
