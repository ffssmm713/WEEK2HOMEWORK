<template>
    <!--  eslint-disable  -->
   <div>
   <el-form  :model="tableData" label-width="80px" inline="true" size="small">
     <el-form-item label="用户名称">
       <el-input v-model="tableData.name"></el-input>
     </el-form-item>
     <el-form-item label="创建时间">
       <el-date-picker type="datetime" placeholder="起始时间" 
         v-model="tableData.mincreatetime" class="datepicker" />
       <el-date-picker type="datetime" placeholder="截止时间" 
         v-model="tableData.maxcreattime" class="datepicker" />
     </el-form-item>
 
     <el-form-item>
       <el-button type="primary" size="mini" @click="getUserList">搜索</el-button>
       <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
     </el-form-item>
   </el-form>
 
   <div>
     <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleCreateUser">新增</el-button>
     <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete">删除</el-button>
     <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
   </div>
 
   <el-table :data="tableData.list" @selection-change="val => tableData.selection = val"
        @sort-change="handlesortchange">
     <el-table-column type="index" width="60" />
     <el-table-column type="selection" width="50" />
     <el-table-column width="50">
       <template slot-scope="scope">
         <img :id="'avatar-' + scope.row.id" class="table-avatar" />
       </template>
     </el-table-column>  
     <el-table-column prop="userName" label="用户名" sortable="custom" />
     <el-table-column prop="trueName" label="奥实姓名" sortable="custom" />
     <el-table-column prop="roleList" label="角色" sortable="custom" />
     <el-table-column prop="createTime" label="创建时间" sortable="custom" />
     <el-table-column prop="status" label="是否激活" sortable="custom" width="100">
       <template slot-scope="scope">
         <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleSwitch(scope.row)" />
       </template>
     </el-table-column>
     <el-table-column label="操作">
       <template slot-scope="scope">
         <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
         <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">删除</el-button>
       </template>
     </el-table-column>
   </el-table>

   <!--分页-->
   <!-- <el-pagination
   layout="prev, pager, next"
   :total="1000">
 </el-pagination> -->
 <el-pagination
 class="pagination"
 :current-page.sync="tableData.pageNum"
 :page-sizes="[10, 20, 30, 40]"
 :page-size.sync="tableData.pagesize"
 layout="total, sizes, prev, pager, next, jumper"
 :total="tableData.total"
 @size-change="getUserList"
 @current-change="getUserList" />

   
 <el-dialog class="user-edit-dialog" :title="userEditForm.id ? '用户编辑' : '新增用户'" 
 :visible.sync="userEditDialogVisible" 
 width="50%" top="8vh">
 <el-form
   ref="userEditForm"
   status-icon
   :model="userEditForm" 
   label-width="80px"
   :rules="userEditForm.id ? userUpdateRules : userCreateRules">

   <el-form-item label="用户名" prop="userName">
     <el-input v-model="userEditForm.userName" />
   </el-form-item>
   
   <el-form-item label="真实姓名" prop="trueName">
     <el-input v-model="userEditForm.trueName" />
   </el-form-item>
   
   <el-form-item label="密码" prop="password">
     <el-input v-model="userEditForm.password" />
   </el-form-item>
   
   <el-form-item label="邮箱" prop="email">
     <el-input v-model="userEditForm.email" />
   </el-form-item>
   
   <el-form-item label="性别">
     <el-radio-group v-model="userEditForm.gender">
       <el-radio :label="0">男</el-radio>
       <el-radio :label="1">女</el-radio>
     </el-radio-group>
   </el-form-item>
   
   <el-form-item label="地址">
     <el-input v-model="userEditForm.address" />
   </el-form-item>

<el-form-item label="简介">
 <el-input v-model="userEditForm.introduction" />
</el-form-item>

<el-form-item label="电话">
 <el-input v-model="userEditForm.phone" />
</el-form-item>

<el-form-item label="角色" prop="roleIds">
 <el-select v-model="userEditForm.roleIds" multiple placeholder="请选择角色">
   <el-option v-for="role in allRoles" :key="role.id" :label="role.name" :value="role.id" />
 </el-select>
</el-form-item>

<!-- <el-form-item label="头像">
 <el-upload
   class="avatar-uploader"
   action=""
   :auto-upload="false"
   :show-file-list="false"
   :on-change="file => handleAvatarChange(file)">
   <img v-if="avatarUploadData.url" :src="avatarUploadData.url" class="avatar" />
   <i v-else class="el-icon-plus avatar-uploader-icon"></i>
 </el-upload>
 <el-button v-if="avatarUploadData.raw" size="mini" @click="resetUploadData(false)">重置</el-button>
</el-form-item> -->

<span slot="footer" class="dialog-footer">
 <el-button @click="userEditDialogVisible = false">取 消</el-button>
 <el-button type='primary' @click="addQrUpdateUser">确 定</el-button>
</span>

   
 </el-form>
</el-dialog> 

</div>
 </template>
 
 
 <script>
 export default {
   name: 'user',
   data() {
     return {
       tableData:{
           name:'',
           mincreatetime:'',
           maxcreattime:'',
           list:[{
               id:1,
               userName:'张三',
               trueName:'222',
               roleList:[],
               status:true,
           }],
           selection:'',
           pageNum:1,
           pagesize:10,
           total:1,
       },
       userEditForm:{
       id:'',
       userName:'',
       trueName:'',
       password:'',
       email:'',
       gender:'',
       address:'',
       introduction:'',
       phone:'',
       roleIds:[],
   },
       userEditDialogVisible: false,
       allRoles:[]
     }
   },
   methods: {
   getUserList() {
     // 查询用户列表
   },
   resetQuery() {
     // 重置查询条件
   },
   handleCreateUser() {
     // 处理新增用户
     this.userEditDialogVisible = true
   },
   handleDelete(ids) {
     // 处理删除用户
   },
   handleImport() {
     // 处理导入用户
   },
   handlesortchange({ prop, order }) {
     // 处理表格排序
   },
   handleSwitch(row) {
     // 处理状态开关的切换
   },
   handleEdit(row) {
     // 处理编辑用户
   },
   handleAvatarChange(file) {
     // 处理头像上传
   },
   resetUploadData() {
     // 重置头像上传数据
   },
   addQrUpdateUser() {
     // 添加或更新用户
   },
 },
 }
 </script>
 
 <style>
.datepicker{
   margin-right: 10px;
   width: 100px;
}

</style>