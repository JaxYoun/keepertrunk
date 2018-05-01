import React from 'react';
import { Link } from 'bisheng/router';
import collect from 'bisheng/collect';
import DocumentTitle from 'react-document-title';
import Layout from './Layout';
import config from '../config';

function getTags(posts) {
  const tags = {};
  Object.keys(posts).forEach(filename => {
    const post = posts[filename];
    const postTag = post.meta.tag;
    if (postTag) {
      if (tags[postTag]) {
        tags[postTag].push(post);
      } else {
        tags[postTag] = [post];
      }
    }
  });
  return tags;
}

const Post = props => {
  const { pageData, utils, picked } = props;
  const { meta, description, content } = pageData;
  const tags = getTags(picked.posts);
  const tagsOrderby = {}
  for (let key of config.menu) {
    tagsOrderby[key] = tags[key]
  }
  console.log(content);
  return (
    <DocumentTitle title={`${meta.title} | 创意信息-在线帮助文档`}>
      <Layout {...props} tags={tagsOrderby}>
        {
          <div className="menu-list">
            {Object.keys(tagsOrderby).map(tag =>
              [
                <a className="item-tag" href={`#${tag}`} id={tag} key="tag">
                  ♦{tag}
                </a>,
              ].concat(
                tagsOrderby[tag].map(({ meta, description }, index) => (
                  <div className="item" key={index}>
                    <h2 className="item-title">
                      <Link to={`${meta.filename.replace(/\.md/, '')}`}>
                        {meta.title}
                      </Link>
                    </h2>
                  </div>
                )),
              ),
            )}
          </div>
        }
        <div className="main-container">
          <h1 className="entry-title">{meta.title}</h1>
          <div className="entry-content">{utils.toReactComponent(content)}</div>
          <div className="entry-meta">
            <span>{`文档编辑：${meta.author || 'unknown'}`}</span>
            <span>{`编辑日期：${meta.publishDate.slice(0, 10)}`}</span>
          </div>
        </div>
      </Layout>
    </DocumentTitle>
  );
};

export default collect(async nextProps => {
  if (!nextProps.pageData) {
    throw 404;
  }
  const pageData = await nextProps.pageData();
  return { pageData };
})(Post);

// TODO
// {%- if config.disqus %}
// {%- include "_disqus.html" %}
// {%- endif %}
// {%- if config.duoshuo %}
// {%- include "_duoshuo.html" %}
// {%- endif %}
