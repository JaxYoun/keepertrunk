---
title: Ttable
tag: 前端开发
publishDate: 2018-03-13
---

# Ttable 后端分页表格

展示行列数据。

## 何时使用

* 当有大量结构化的数据需要展现时；

* 当需要对数据进行排序、搜索、分页、自定义操作等复杂行为时。

* 当数据使用后端分页时

## 如何使用

指定 表数据的列信息，指定 后端分页数据的的 api 路径，以下是一个简单是例子。

```jsx
const sourceUrl = '/test/testUser';
const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
  },
  {
    title: '年龄',
    dataIndex: 'age',
  },
  {
    title: '住址',
    dataIndex: 'address',
  },
];

<Table dataSource={dataSource} columns={columns} />;
```

## API

### Table

| 参数                   | 说明                                                                                    | 类型                                  | 默认值                                                                                                                                                              |
| ---------------------- | --------------------------------------------------------------------------------------- | ------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| bordered               | 是否展示外边框和列边框                                                                  | boolean                               | false                                                                                                                                                               |
| columns                | 表格列的配置描述，具体项见下表                                                          | ColumnProps[]                         | -                                                                                                                                                                   |
| components             | 覆盖默认的 table 元素                                                                   | object                                | -                                                                                                                                                                   |
| dataSource             | 数据数组                                                                                | any\[]                                |                                                                                                                                                                     |
| defaultExpandAllRows   | 初始时，是否展开所有行                                                                  | boolean                               | false                                                                                                                                                               |
| defaultExpandedRowKeys | 默认展开的行                                                                            | string\[]                             | -                                                                                                                                                                   |
| expandedRowKeys        | 展开的行，控制属性                                                                      | string\[]                             | -                                                                                                                                                                   |
| expandedRowRender      | 额外的展开行                                                                            | Function(record):ReactNode            | -                                                                                                                                                                   |
| expandRowByClick       | 通过点击行来展开子行                                                                    | boolean                               | `false`                                                                                                                                                             |
| footer                 | 表格尾部                                                                                | Function(currentPageData)             |                                                                                                                                                                     |
| indentSize             | 展示树形数据时，每层缩进的宽度，以 px 为单位                                            | number                                | 15                                                                                                                                                                  |
| loading                | 页面是否加载中                                                                          | object                                | false                                                                                                                                                               |
| locale                 | 默认文案设置，目前包括排序、过滤、空数据文案                                            | object                                | filterConfirm: '确定' <br> filterReset: '重置' <br> emptyText: '暂无数据' <br> [默认值](https://github.com/ant-design/ant-design/issues/575#issuecomment-159169511) |
| pagination             | 分页器，配置项参考 [pagination](/components/pagination/)，设为 false 时不展示和进行分页 | object                                |                                                                                                                                                                     |
| rowClassName           | 表格行的类名                                                                            | Function(record, index):string        | -                                                                                                                                                                   |
| rowKey                 | 表格行 key 的取值，可以是字符串或一个函数                                               | string\|Function(record):string       | 'key'                                                                                                                                                               |
| rowSelection           | 列表项是否可选择，[配置项](#rowSelection)                                               | object                                | null                                                                                                                                                                |
| scroll                 | 横向或纵向支持滚动，也可用于指定滚动区域的宽高度：`{{ x: true, y: 300 }}`               | object                                | -                                                                                                                                                                   |
| showHeader             | 是否显示表头                                                                            | boolean                               | true                                                                                                                                                                |
| size                   | 正常或迷你类型，`default` or `small`                                                    | string                                | default                                                                                                                                                             |
| title                  | 表格标题                                                                                | Function(currentPageData)             |                                                                                                                                                                     |
| onChange               | 分页、排序、筛选变化时触发                                                              | Function(pagination, filters, sorter) |                                                                                                                                                                     |
| onExpand               | 点击展开图标时触发                                                                      | Function(expanded, record)            |                                                                                                                                                                     |
| onExpandedRowsChange   | 展开的行变化时触发                                                                      | Function(expandedRows)                |                                                                                                                                                                     |
| onHeaderRow            | 设置头部列属性                                                                          | Function(column, index)               | -                                                                                                                                                                   |
| onRow                  | 设置列属性                                                                              | Function(record, index)               | -                                                                                                                                                                   |

### Column

列描述数据对象，是 columns 中的一项，Column 使用相同的 API。

| 参数                          | 说明                                                                                                                                                                                         | 类型                             | 默认值 |
| ----------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------- | ------ |
| className                     | 列的 className                                                                                                                                                                               | string                           | -      |
| colSpan                       | 表头列合并,设置为 0 时，不渲染                                                                                                                                                               | number                           |        |
| dataIndex                     | 列数据在数据项中对应的 key，支持 `a.b.c` 的嵌套写法                                                                                                                                          | string                           | -      |
| filterDropdown                | 可以自定义筛选菜单，此函数只负责渲染图层，需要自行编写各种交互                                                                                                                               | ReactNode                        | -      |
| filterDropdownVisible         | 用于控制自定义筛选菜单是否可见                                                                                                                                                               | boolean                          | -      |
| filtered                      | 标识数据是否经过过滤，筛选图标会高亮                                                                                                                                                         | boolean                          | false  |
| filteredValue                 | 筛选的受控属性，外界可用此控制列的筛选状态，值为已筛选的 value 数组                                                                                                                          | string\[]                        | -      |
| filterIcon                    | 自定义 fiter 图标。                                                                                                                                                                          | ReactNode                        | false  |
| filterMultiple                | 是否多选                                                                                                                                                                                     | boolean                          | true   |
| filters                       | 表头的筛选菜单项                                                                                                                                                                             | object\[]                        | -      |
| fixed                         | 列是否固定，可选 `true`(等效于 left) `'left'` `'right'`                                                                                                                                      | boolean\|string                  | false  |
| key                           | React 需要的 key，如果已经设置了唯一的 `dataIndex`，可以忽略这个属性                                                                                                                         | string                           | -      |
| render                        | 生成复杂数据的渲染函数，参数分别为当前行的值，当前行数据，行索引，@return 里面可以设置表格[行/列合并](#components-table-demo-colspan-rowspan)                                                | Function(text, record, index) {} | -      |
| sorter                        | 排序函数，本地排序使用一个函数(参考 [Array.sort](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort) 的 compareFunction)，需要服务端排序可设为 true | Function\|boolean                | -      |
| sortOrder                     | 排序的受控属性，外界可用此控制列的排序，可设置为 `'ascend'` `'descend'` `false`                                                                                                              | boolean\|string                  | -      |
| title                         | 列头显示文字                                                                                                                                                                                 | string\|ReactNode                | -      |
| width                         | 列宽度                                                                                                                                                                                       | string\|number                   | -      |
| onCell                        | 设置单元格属性                                                                                                                                                                               | Function(record)                 | -      |
| onFilter                      | 本地模式下，确定筛选的运行函数                                                                                                                                                               | Function                         | -      |
| onFilterDropdownVisibleChange | 自定义筛选菜单可见变化时调用                                                                                                                                                                 | function(visible) {}             | -      |
| onHeaderCell                  | 设置头部单元格属性                                                                                                                                                                           | Function(column)                 | -      |

### ColumnGroup

| 参数  | 说明         | 类型              | 默认值 |
| ----- | ------------ | ----------------- | ------ |
| title | 列头显示文字 | string\|ReactNode | -      |

### rowSelection

选择功能的配置。

| 参数                  | 说明                                                            | 类型                                         | 默认值     |
| --------------------- | --------------------------------------------------------------- | -------------------------------------------- | ---------- |
| getCheckboxProps      | 选择框的默认属性配置                                            | Function(record)                             | -          |
| fixed                 | 把选择框列固定在左边                                            | boolean                                      | -          |
| hideDefaultSelections | 去掉『全选』『反选』两个默认选项                                | boolean                                      | false      |
| selectedRowKeys       | 指定选中项的 key 数组，需要和 onChange 进行配合                 | string\[]                                    | \[]        |
| selections            | 自定义选择项 [配置项](#selection), 设为 `true` 时使用默认选择项 | object\[]\|boolean                           | true       |
| type                  | 多选/单选，`checkbox` or `radio`                                | string                                       | `checkbox` |
| onChange              | 选中项发生变化的时的回调                                        | Function(selectedRowKeys, selectedRows)      | -          |
| onSelect              | 用户手动选择/取消选择某列的回调                                 | Function(record, selected, selectedRows)     | -          |
| onSelectAll           | 用户手动选择/取消选择所有列的回调                               | Function(selected, selectedRows, changeRows) | -          |
| onSelectInvert        | 用户手动选择反选的回调                                          | Function(selectedRows)                       | -          |

### selection

| 参数     | 说明                       | 类型                        | 默认值 |
| -------- | -------------------------- | --------------------------- | ------ |
| key      | React 需要的 key，建议设置 | string                      | -      |
| text     | 选择项显示的文字           | string\|React.ReactNode     | -      |
| onSelect | 选择项点击回调             | Function(changeableRowKeys) | -      |

## 添加了以下属性:

| 参数           | 说明                                                                                                                  | 类型                         | 默认值 |
| -------------- | --------------------------------------------------------------------------------------------------------------------- | ---------------------------- | ------ |
| sourceUrl      | 数据查询接口链接                                                                                                      | string                       | -      |
| fields         | 请求参数                                                                                                              | object                       | {}     |
| requiredFields | 该参数名为'`必填参数`', 当`fields`内的属性没有完全包含`requiredFields`内数组成员时，`Ttable` 组件不会发起数据查询请求 | array                        | []     |
| tableTime      | 更行`tableTime`, 刷新表数据。推荐使用`new Date`。                                                                     | string                       | ''     |
| serialNumber   | 显示序号                                                                                                              | boolean                      | false  |
| loadedBack     | 返回查询数据                                                                                                          | Function(response, pageinfo) | -      |

### Column

| 参数 | 说明                                                                                            | 类型    | 默认值 |
| ---- | ----------------------------------------------------------------------------------------------- | ------- | ------ |
| tip  | 在该列未设置`render`且 `tip` 为`true`时，省略显示该列内容，且在鼠标悬停时弹出气泡并显示全部内容 | boolean | false  |

```jsx
import React from 'react';
import { Form, Button, Input, Card } from 'antd';
import { Ttable } from 'components';

class DataList extends React.Component {
  constructor(props) {
    super(props);
  }
  state = {
    fields: {
      name: '',
      phone: '1',
    },
    tableTime: '',
    // fields 数据查询参数,可以用于数据筛选。
    // tableTime 存储表的时间，当进行某些操作后，给该变量赋值 new Date(), 可进行数据刷新操作。
    // 当这两个参数发生变化时 均自动更新表数据，
  };
  loadedBack = (response, pageInfo) => {
    console.log(response);
    console.log(pageInfo);
  };
  render() {
    const { fields, tableTime } = this.state;

    const sourceUrl = '/test/testUser'; // 数据接口路径
    const columns = [
      {
        title: '姓名',
        dataIndex: 'name',
      },
      {
        title: '年龄',
        dataIndex: 'age',
      },
      {
        title: 'Phone',
        dataIndex: 'phone',
        key: 'phone',
      },
      {
        title: '住址',
        dataIndex: 'address',
        tip: true, // 在列里面添加 tip 属性
      },
    ];
    const requiredFields = ['phone']; // 当设置了该属性，参数 fields 的对象属性必须有 'phone', 否则不会发起数据请求。

    return (
      <div>
        <Card>
          <Ttable
            sourceUrl={sourceUrl}
            columns={columns}
            fields={fields}
            requiredFields={requiredFields}
            tableTime={tableTime}
            serialNumber // 自动在每列前面加上序号
            loadedBack={this.loadedBack} // 获取请求的返回数据和分页信息
          />
        </Card>
      </div>
    );
  }
}

export default DataList;
```
