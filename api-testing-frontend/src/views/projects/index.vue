<template>
  <div class="app-container">
    <!--  Modal for project creation  -->
    <el-dialog title="Type project name" :visible.sync="showModal">
      <el-form>
        <el-form-item>
          <el-input
            v-model="newProjectName"
            autocomplete="off" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showModal = false">Cancel</el-button>
        <el-button
          type="primary"
          @click="onConfirm"
          v-bind:disabled="!validateProjectName()"
        >Confirm</el-button>
      </span>
      <p :class="[validateProjectName() ? 'green' : 'red']">
        * Hostname must not contain a white space! <br />
        * Hostname must contain a dot! <br />
        * Hostname's length must be greater than 3!
      </p>
    </el-dialog>

    <!--  Create project button  -->
    <el-row>
      <el-col :offset="22">
        <el-button
          type="primary"
          @click="onCreate"
          icon="el-icon-plus"
        >Create
        </el-button>
      </el-col>
    </el-row>

    <!-- Project table -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
    >
      <el-table-column align="center" width="90">
        <template slot-scope="scope">
          <router-link :to="'/requests/'+scope.$index">
            <el-button
              icon="el-icon-search"
              circle
              type="primary"
            />
          </router-link>
        </template>
      </el-table-column>

      <el-table-column label="Project Name" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.hostname }}
        </template>
      </el-table-column>

      <el-table-column width="90" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onDelete(list[scope.$index].hostname)"
            icon="el-icon-delete"
            circle
            type="danger"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getProjects, createProject, deleteProject } from '@/api/projects'

export default {
  data() {
    return {
      newProjectName: '',
      showModal: false,

      list: null,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getProjects().then(response => {
        console.log('Response:' + response)
        this.list = response
        this.listLoading = false
      })
        .catch(error => {
          console.log(error)
        })
    },
    onCreate() {
      this.showModal = true
    },
    onConfirm() {
      this.showModal = false
      createProject(this.newProjectName).then(response => {
        this.fetchData()
        this.newProjectName = ''
      })
    },
    onDelete(hostname) {
      this.$confirm(hostname + ' will be deleted. Are you sure?')
        .then(_ => {
          deleteProject(hostname).then(response => {
            this.fetchData()
          })
        })
        .catch(_ => {})
    },
    validateProjectName() {
      const res = this.newProjectName.includes('.') &&
        !(this.newProjectName.length < 4) &&
        !this.newProjectName.includes(' ')
      return res
    }
  }
}
</script>

<style scoped>
  .red {
    color: red;
    font-size: x-small;
  }

  .green {
    color: green;
    font-size: x-small;
  }

  .el-row {
    margin-bottom: 20px;
  }
</style>

