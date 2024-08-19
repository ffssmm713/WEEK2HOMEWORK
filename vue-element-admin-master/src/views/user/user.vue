<template>
  <div class="app-container">

    <!-- 用户搜索表单 -->
    <el-form :model="tableData" size="small" :inline="true" label-width="68px">
      <el-form-item label="用户名称">
        <el-input
          v-model="tableData.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="getUserList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 用户操作按钮 -->
    <div>
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleCreateUser"> 新增 </el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleBatchDelete"> 删除</el-button>
    </div>

    <!-- 用户列表 -->
    <el-table
      :data="tableData.list"
      @selection-change="val => tableData.selection = val"
      @sort-change="handleSortChange"
    >
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
      <el-table-column width="50">
        <template slot-scope="scope">
          <!-- <img class="table-avatar" :src="scope.row.avatar" /> -->
          <!-- 直接使用:src绑定会导致刷新失败 -->
          <img :id="'avatar-' + scope.row.id" class="table-avatar">
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="用户名" sortable="custom" />
      <el-table-column prop="trueName" label="真实姓名" sortable="custom" />
      <el-table-column prop="email" label="电子邮箱" sortable="custom" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">
            编辑 </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">
            删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="pagination"
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="getUserList"
      @current-change="getUserList"
    />

    <!-- 用户编辑/创建窗口 -->
    <el-dialog class="user-edit-dialog" :title="userEditForm.id ? '用户编辑' : '新增用户'" :visible.sync="userEditDialogVisible" width="50%" top="8vh">
      <el-form
        ref="userEditForm"
        status-icon
        :model="userEditForm"
        label-width="80px"
        :rules="userEditForm.id ? userUpdateRules : userCreateRules"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userEditForm.userName" />
        </el-form-item>
        <el-form-item label="真实姓名">
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
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="userEditDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdateUser">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from 'axios' // 使用 axios 进行 HTTP 请求

export default {
  name: 'User',
  data() {
    return {
      tableData: {
        userName: '',
        list: [{
          id: 1,
          userName: '张三',
          trueName: '222',
          email: ''
        }],
        selection: '',
        pageNum: 1,
        pagesize: 10,
        total: 1
      },
      userEditForm: {
        id: '',
        userName: '',
        trueName: '',
        password: '',
        email: '',
        gender: '',
        address: '',
        introduction: '',
        phone: ''
      },
      userEditDialogVisible: false
    }
  },
  mounted() {
    // 页面加载时调用 getUserById 方法，假设获取 ID 为 1 的用户信息
    this.getUserList()
  },

  methods: {
    handleCreateUser() {
      // 处理新增用户
      this.userEditDialogVisible = true
    },
    getUserList(sortBy = 'id', sortOrder = 'asc') {
      console.log(`Fetching data with sortBy: ${sortBy} and sortOrder: ${sortOrder}`) // 调试输出
      axios.get('http://localhost:8080/users', {
        params: {
          userName: this.tableData.userName || '', // 当用户名为空时传递空字符串
          pageNum: this.tableData.pageNum,
          pageSize: this.tableData.pageSize,
          sortBy: 'id', // 排序字段
          sortOrder: sortOrder // 排序方式
        }
      })
        .then(response => {
          this.tableData.list = response.data.content
          this.tableData.total = response.data.totalElements
        })
        .catch(error => {
          this.$message.error('获取用户列表失败')
        })
    },

    resetQuery() {
      this.tableData.userName = ''
      this.tableData.pageNum = 1 // 重置页码为 1
      this.tableData.pageSize = 10 // 重置每页条数
      this.getUserList() // 重置后重新加载用户列表
    },
    handleSortChange({ column, prop = 'id', order }) {
    // 处理排序变化
      console.log('Column:', column)
      console.log('Prop:', prop)
      console.log('Order:', order)
      const sortBy = prop // 排序字段
      const sortOrder = order === 'ascending' ? 'asc' : 'desc' // 排序方式
      this.getUserList(sortBy, sortOrder) // 重新获取用户列表
    },
    addOrUpdateUser() {
      if (this.userEditForm.id) {
      // 如果存在 id，执行更新操作
        this.updateUser()
      } else {
      // 不存在 id，执行新增操作
        this.createUser()
      }
    },
    createUser() {
      this.$refs.userEditForm.validate(valid => {
        if (valid) {
        // 通过 axios 向后端发送 POST 请求
          axios.post('http://localhost:8080/users', this.userEditForm)
            .then(response => {
              this.$message.success('用户新增成功')
              this.userEditDialogVisible = false
              this.getUserList() // 刷新用户列表
            })
            .catch(error => {
              this.$message.error('新增用户失败')
            })
        }
      })
    },
    updateUser() {
      axios.put(`http://localhost:8080/users/${this.userEditForm.id}`, this.userEditForm)
        .then(response => {
          this.$message.success('用户更新成功')
          this.userEditDialogVisible = false
          this.getUserList() // 刷新用户列表
        })
        .catch(error => {
          this.$message.error('更新用户失败')
        })
    },
    // 点击编辑按钮时触发
    handleEdit(row) {
    // 将当前用户数据填充到表单中
      this.userEditForm = { ...row }
      this.userEditDialogVisible = true
    },

    // 删除用户
    handleDelete(ids) {
      this.$confirm('确定删除选中的用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`http://localhost:8080/users/${ids.join(',')}`)
          .then(response => {
            this.$message.success('用户删除成功')
            this.getUserList() // 刷新用户列表
          })
          .catch(error => {
            this.$message.error('删除用户失败')
          })
      }).catch(() => {
      // 用户取消删除操作
      })
    },
    // 批量删除
    handleBatchDelete() {
      if (this.tableData.selection.length === 0) {
        this.$message.warning('请至少选择一个用户进行删除')
        return
      }

      // 提取所有选中用户的 ID
      const ids = this.tableData.selection.map(user => user.id)

      this.$confirm('确定删除选中的用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
      // 使用 Promise.all 并行执行多个删除请求
        const deletePromises = ids.map(id => {
          return axios.delete(`http://localhost:8080/users/${id}`)
        })

        // 等待所有删除请求完成后刷新列表
        Promise.all(deletePromises)
          .then(() => {
            this.$message.success('批量删除成功')
            this.getUserList() // 刷新用户列表
          })
          .catch(() => {
            this.$message.error('批量删除部分或全部失败')
          })
      }).catch(() => {
      // 用户取消删除操作
      })
    }

  }

}

</script>

<style>
.datepicker{
 margin-right: 10px;
 width: 100px;
}

</style>
