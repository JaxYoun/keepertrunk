import React from 'react';
import { Link } from 'bisheng/router';
import DocumentTitle from 'react-document-title';
import Layout from './Layout';

function getTime(date) {
  return (new Date(date)).getTime();
}

export default (props) => {
  window.location.href = '/posts/help/help';
  return null;
}

// TODO
// <div class="pagination">
//   {%- if pagination.has_prev %}
//   <a class="newer" href="{{ pagination_url(pagination.prev_num) }}">Newer</a>
//   {%- endif %}

//   {%- if pagination.has_next %}
//   <a class="older" href="{{ pagination_url(pagination.next_num) }}">Older</a>
//   {%- endif %}
// </div>
