import React from 'react';
import { Link } from 'bisheng/router';
import '../static/style';

export default ({ themeConfig, tags, children }) => {
  console.log('layout tags', tags);
  return (
    <div>
      <div className="header">
        <div className="container">
          <div className="brand">
            <img src="/logo.png" alt={themeConfig.sitename} />
            {!themeConfig.tagline ? null : (
              <span className="tagline">{themeConfig.tagline}</span>
            )}
          </div>
        </div>
        <div className="tagcloud">
          {Object.keys(tags).map((tag, index) => (
            <a href={`#${tag}`} key={index}>
              {tag}
            </a>
          ))}
          {!themeConfig.navigation
            ? null
            : themeConfig.navigation.map((item, index) => (
                <a href={item.link} target="_blank" key={index}>
                  {item.title}
                </a>
              ))}
        </div>
      </div>
      <div className="document yue">{children}</div>
      <div className="footer">
        {themeConfig.footer ? themeConfig.footer : null}
      </div>
    </div>
  );
};
