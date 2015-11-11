Kingcore Framework 主要特性介绍：

> > 支持多数据源，灵活的DataSource配置方式，既可以使用框架提供的DataSource，也可以使用任何其他第三方DataSource的实现；

> > 跨数据库，数据库差异部分采用接口编程，编程时候不需要考虑使用的是哪种数据库。系统提供Oracle、MySQL、SqlServer等主流数据库的实现，也可以编写自己的数据库接口实现，自定义自己的数据库方言的处理、主键生成机制等；

> > 丰富的DAO基类封装，提供各种数据库update和批处理方法，支持预编译方式，多达15种数据库查询方法，分页查询，提供最好的使用体验；提供数据库的Clob,Blob处理。分页查询、lob类型处理全部采用接口方式实现，只需系统初始配置，开发时候无需关心是哪种数据库；

> > 灵活的ResultSet 与 Java POJO的映射方式，采用接口编程，系统提供几种常用实现，也可以根据需要自定义实现，定制自己的映射规则，如数据库字段 user\_name映射为POJO的userName 还是 user\_name属性，可以自定义；

> > 丰富的事务管理方式，既可以支持简单的connection共用，也提供复杂的事务管理TransactionManager实现，支持多数据源事务，支持多事务嵌套；

> > 支持jdbc新API：RowSet，只需要在框架中加入RowSet实现jar包即可，编程中只需用接口编程；

> > 提供丰富的Jsp Tag库，包括一整套对RowSet的操作标签，简化页面代码；

> > 框架完全基于POJO，支持IoC方式初始，也支持编码式初始；

> > 基于LGPL协议开源；

> > 基于部分现有的开源框架、或提供与现有框架的整个，紧密结合实际项目开发需要，使开发更加规范、高效、快捷；

开源项目地址：https://kingcoreframework.googlecode.com/

更多图文介绍：http://user.qzone.qq.com/42905212/blog/1312953637

