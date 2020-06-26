<template>
  <div class="app-container">
    <!--  Fuzzing Modal  -->
    <el-dialog title="Fuzzing" :visible.sync="showModal" @close="closeFuzzingModal()">
      <el-form
        class="fuzzingModal"
        ref="form"
        :model="fuzzingReq"
        label-width="120px"
        label-position="top" >
        <!--    URL    -->
        <el-form-item label="Url">
          <el-col v-if="isEditingFuzzingUrl" :span="22">
            <el-input style="width: 100%" v-model="fuzzingReq.url" />
          </el-col>
          <el-col v-else :span="22">{{ fuzzingReq.url }}</el-col>
          <el-col class="fuzzingRow" :offset="1" :span="1">
            <el-button
              @click="isEditingFuzzingUrl=!isEditingFuzzingUrl"
              type="info"
              icon="el-icon-edit"
              size="mini"
              circle />
          </el-col>
        </el-form-item>
        <!--    Method    -->
        <el-form-item label="Method">
          <el-col :span="22">
            <el-select v-model="fuzzingReq.method">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
              <el-option label="PATCH" value="PATCH" />
            </el-select>
          </el-col>
          <el-col :span="1" :offset="1">
            <el-button
              @click="fuzzingReq.method = originalMethodBeforeFuzzing"
              type="info"
              icon="el-icon-refresh"
              size="mini"
              circle />
          </el-col>
        </el-form-item>
        <!--    Headers    -->
        <el-form-item label="Headers">
          <el-row class="fuzzingRow">
            <el-col class="fuzzingRow" :span="1">
              <el-button
                v-if="!isCreatingHeader"
                @click="isCreatingHeader=true"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                circle />
            </el-col>
            <el-col :span="5" :offset="isCreatingHeader ? 2 : 1" class="bold">KEY</el-col>
            <el-col :span="5" class="bold">VALUE</el-col>
            <el-col class="fuzzingRow" :span="1" :offset="11">
              <el-button
                @click="isEditingFuzzingHeaders=!isEditingFuzzingHeaders"
                type="info"
                icon="el-icon-edit"
                size="mini"
                circle />
            </el-col>
          </el-row>

          <el-row class="fuzzingRow" v-if="isCreatingHeader">
            <el-col :span="2">
              <el-button
                @click="isCreatingHeader=false; newHeaderValue=''; newHeaderKey=''"
                type="danger"
                icon="el-icon-delete"
                size="mini"
                circle />
            </el-col>
            <el-col :span="5">
              <el-input v-model="newHeaderKey" placeholder="Key" />
            </el-col>
            <el-col :span="15">
              <el-input v-model="newHeaderValue" placeholder="Value" />
            </el-col>
            <el-col :span="1" :offset="1">
              <el-button
                :disabled="newHeaderKey === ''"
                @click="createHeaderButtonClicked()"
                type="success"
                icon="el-icon-check"
                size="mini"
                circle />
            </el-col>
          </el-row>

          <el-row class="fuzzingRow" v-for="(value, key) in fuzzingReq.headers">
            <el-col :span="2">
              <!-- we needed to update the form, so just negate the boolean value twice :) -->
              <el-button
                @click="delete fuzzingReq.headers[key]; isEditingFuzzingHeaders=!isEditingFuzzingHeaders; isEditingFuzzingHeaders=!isEditingFuzzingHeaders"
                type="danger"
                icon="el-icon-delete"
                size="mini"
                circle />
            </el-col>
            <template v-if="isEditingFuzzingHeaders">
              <el-col :span="5"> {{key}} </el-col>
              <el-col :span="17">
                <el-input style="width: 100%;" v-model="fuzzingReq.headers[key]" />
              </el-col>
            </template>
            <template v-else>
              <el-col :span="5"> {{key}} </el-col>
              <el-col :span="16"> {{value}}</el-col>
            </template>
          </el-row>
        </el-form-item>
        <!--    Content    -->
        <el-form-item label="Content" v-if="fuzzingReq.content !== null">
          <template v-if="typeof fuzzingReq.content === 'string' || typeof fuzzingReq.content === 'number'">
            <el-col v-if="isEditingFuzzingContentString" :span="22">
              <el-input style="width: 100%" v-model="fuzzingReq.content" />
            </el-col>
            <el-col v-else :span="22">{{ fuzzingReq.content }}</el-col>
            <el-col class="fuzzingRow" :offset="1" :span="1">
              <el-button
                @click="isEditingFuzzingContentString=!isEditingFuzzingContentString"
                type="info"
                icon="el-icon-edit"
                size="mini"
                circle />
            </el-col>
          </template>
          <template v-else-if="typeof  fuzzingReq.content === 'object'">
            <el-row class="fuzzingRow">
              <el-col class="fuzzingRow" :span="1">
                <el-button
                  v-if="!isCreatingContent"
                  @click="isCreatingContent=true"
                  type="primary"
                  icon="el-icon-plus"
                  size="mini"
                  circle />
              </el-col>
              <el-col :span="5" :offset="isCreatingContent ? 2 : 1" class="bold">KEY</el-col>
              <el-col :span="5" class="bold">VALUE</el-col>
              <el-col class="fuzzingRow" :span="1" :offset="11">
                <el-button
                  @click="isEditingFuzzingContent=!isEditingFuzzingContent"
                  type="info"
                  icon="el-icon-edit"
                  size="mini"
                  circle />
              </el-col>
            </el-row>

            <el-row class="fuzzingRow" v-if="isCreatingContent">
              <el-col :span="2">
                <el-button
                  @click="isCreatingContent=false; newContentKey=''; newContentValue=''"
                  type="danger"
                  icon="el-icon-delete"
                  size="mini"
                  circle />
              </el-col>
              <el-col :span="5">
                <el-input v-model="newContentKey" placeholder="Key" />
              </el-col>
              <el-col :span="15">
                <el-input v-model="newContentValue" placeholder="Value" />
              </el-col>
              <el-col :span="1" :offset="1">
                <el-button
                  :disabled="newContentKey === ''"
                  @click="createContentButtonClicked()"
                  type="success"
                  icon="el-icon-check"
                  size="mini"
                  circle />
              </el-col>
            </el-row>

            <el-row class="fuzzingRow" v-for="(value, key) in fuzzingReq.content">
              <el-col :span="2">
                <!-- we needed to update the form, so just negate the boolean value twice :) -->
                <el-button
                  @click="delete fuzzingReq.content[key]; isEditingFuzzingContent=!isEditingFuzzingContent; isEditingFuzzingContent=!isEditingFuzzingContent"
                  type="danger"
                  icon="el-icon-delete"
                  size="mini"
                  circle />
              </el-col>
              <template v-if="isEditingFuzzingContent">
                <el-col :span="5"> {{key}} </el-col>
                <el-col :span="17">
                  <el-input style="width: 100%;" v-model="fuzzingReq.content[key]" />
                </el-col>
              </template>
              <template v-else>
                <el-col :span="5"> {{key}} </el-col>
                <el-col :span="16"> {{value}}</el-col>
              </template>
            </el-row>
          </template>
        </el-form-item>
        <!--    Send Button    -->
        <el-row>
          <el-col :span="4" :offset="18">
            <el-button
              type="primary"
              @click="sendModifiedRequest()">Send Modified Request</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>

    <!--  Hostname Row  -->
    <el-row class="tableRow">
      <el-card class="box-card">
        <el-row>
          <el-col :span="2" class="bold">Hostname:</el-col>
          <el-col :span="20" v-if="hostname">{{ hostname }}</el-col>
          <el-col :span="20" v-else class="warning">No Hostname data</el-col>
        </el-row>
      </el-card>
    </el-row>

    <!--  Requests table  -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      element-loading-text="Loading"
      class="table"
      highlight-current-row
    >
      <!--     Request details, expandable column     -->
      <el-table-column align="center" width="90" type="expand">
        <template slot-scope="scope">
          <!--     Timestamp     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Timestamp:</el-col>
            <el-col :span="21" :offset="0" :class="scope.row['timestamp'] ? '' : 'warning'">
              {{ scope.row['timestamp'] ? scope.row['timestamp'] : 'No Timestamp data' }}
            </el-col>
          </el-row>
          <!--     Status Code     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Status Code:</el-col>
            <el-col :span="21" :offset="0" :class="scope.row['statusCode'] ? '' : 'warning'">
              {{ scope.row['statusCode'] ? scope.row['statusCode'] : 'No Status Code data' }}
            </el-col>
          </el-row>
          <!--     Hostname     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Hostname:</el-col>
            <el-col :span="21" :offset="0" :class="scope.row['hostname'] ? '' : 'warning'">
              {{ scope.row['hostname'] ? scope.row['hostname'] : 'No Hostname data' }}
            </el-col>
          </el-row>
          <!--     ID     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Id:</el-col>
            <el-col :span="21" :offset="0" :class="scope.row['id'] ? '' : 'warning'">
              {{ scope.row['id'] ? scope.row['id'] : 'No ID data' }}
            </el-col>
          </el-row>
          <!--     Method     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Method:</el-col>
            <el-col :span="21" :offset="0">
              <el-tag v-if="scope.row['method']" type="success" effect="light">
                {{ scope.row['method'] }}
              </el-tag>
              <span v-else class="warning">No Method data</span>
            </el-col>
          </el-row>
          <!--     URL     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Url:</el-col>
            <el-col :span="10" :offset="0" :class="scope.row['url'] ? '' : 'warning'">
              {{ scope.row['url'] ? scope.row['url'] : 'No URL data' }}
            </el-col>
          </el-row>
          <!--     Headers     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Headers:</el-col>
            <el-col v-if="parseToJSON(scope.row['headers']) !== null" :span="21">
              <json-viewer :value="parseToJSON(scope.row['headers'])" />
            </el-col>
            <el-col v-else class="warning" :span="21">No Headers data</el-col>
          </el-row>
          <!--     Content     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Content:</el-col>
            <el-col
              v-if="scope.row.headers.includes('application/json') && scope.row.content !== null && scope.row.content !== 'null'"
              :span="21">
              <json-viewer :value="parseToJSON(scope.row['content'])" />
            </el-col>
            <el-col v-else-if="scope.row.content !== null" :span="21">
              {{ scope.row['content'] }}
            </el-col>
            <el-col v-else class="warning" :span="21">No Content data</el-col>
          </el-row>
          <!--     Response     -->
          <el-row class="tableRow">
            <el-col class="bold" :span="3" :offset="0">Response:</el-col>
            <el-col v-if="parseToJSON(scope.row['response']) !== null" :span="21">
              <json-viewer :value="parseToJSON(scope.row['response'])" />
            </el-col>
            <el-col v-else class="warning" :span="21">No Response data</el-col>
          </el-row>
          <!--     Modify Button     -->
          <el-row>
            <el-col :offset="22">
              <el-button type="primary" @click="modifyButtonClicked(scope.$index)">Modify</el-button>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <!--     Timestamp column     -->
      <el-table-column label="Timestamp" width="200" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.timestamp">{{ convertFromEpoch(scope.row.timestamp) }}</span>
          <span v-else class="warning">No Timestamp data</span>
        </template>
      </el-table-column>
      <!--     URL column     -->
      <el-table-column label="URL" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.url">{{ scope.row.url }}</span>
          <span v-else class="warning">No URL data</span>
        </template>
      </el-table-column>
      <!--     Request Type column     -->
      <el-table-column label="Request Type" width="120">
        <template slot-scope="scope">
          <el-tag
            v-if="!scope.row.modified"
            type="success"
            effect="light"> Original </el-tag>
          <el-tag
            v-else-if="scope.row.modified"
            type="danger"
            effect="dark"> Modified </el-tag>
          <span v-else class="warning">No data</span>
        </template>
      </el-table-column>
      <!--     Status Code column     -->
      <el-table-column label="Code" width="120">
        <template slot-scope="scope">
          <el-tag
            v-if="scope.row.statusCode / 100 < 4"
            type="success"
            effect="light"> {{ scope.row.statusCode }} </el-tag>
          <el-tag
            v-else-if="scope.row.statusCode / 100 < 6"
            type="danger"
            effect="light"> {{ scope.row.statusCode }} </el-tag>
          <span v-else-if="scope.row.statusCode" class="warning" style="font-size: 10px">
            Unhandled code
          </span>
          <span v-else class="warning" style="font-size: 10px">No Code data</span>
        </template>
      </el-table-column>
      <!--     Method column     -->
      <el-table-column label="Method" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.method" type="success" effect="light"> {{ scope.row.method }} </el-tag>
          <span v-else class="warning" style="font-size: 10px">No Method data</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { getRequests, modifyRequest } from '@/api/requests'
import store from '@/store'

export default {
  data() {
    return {
      showModal: false,

      originalMethodBeforeFuzzing: '',
      isEditingFuzzingUrl: false,
      isEditingFuzzingHeaders: false,
      isEditingFuzzingContent: false,
      isEditingFuzzingContentString: false,
      isCreatingHeader: false,
      isCreatingContent: false,
      newHeaderKey: '',
      newHeaderValue: '',
      newContentKey: '',
      newContentValue: '',

      hostId: -1,
      hostname: '',
      list: null,
      listLoading: true,
      fuzzingReq: {
        url: '',
        method: '',
        headers: null,
        content: null
      }
    }
  },
  created() {
    this.listLoading = false

    this.hostId = this.$route.params.id
    this.hostname = store.getters.projects[this.hostId].hostname

    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getRequests(this.hostname).then(response => {
        console.log('Response:' + response)

        /* Remove '\' from response */
        response.forEach(function(item) {
          for (const key in item) {
            if (typeof item[key] === 'string') {
              item[key] = item[key].replace(/\\/g, '')
            }

            if (key === 'response') {
              try {
                JSON.parse(item[key])
              } catch {
                if (item[key].length > 100) {
                  item[key] = item[key].substring(0,100) + "..."
                }
              }
            }
          }
        })

        this.list = response
        this.listLoading = false
      })
        .catch(error => {
          console.log(error)
          this.listLoading = false
        })
    },
    modifyButtonClicked(index) {
      // performs a deep copy
      const item = JSON.parse(JSON.stringify(this.list[index]))
      this.showModal = true
      this.fuzzingReq = this.parseToJSON(item)

      this.originalMethodBeforeFuzzing = item.method
      this.fuzzingReq.headers = this.parseToJSON(this.fuzzingReq.headers)

      if ((this.fuzzingReq.headers['Content-Type'] &&
        this.fuzzingReq.headers['Content-Type'].includes('application/json')) ||
        (this.fuzzingReq.headers['content-type'] &&
          this.fuzzingReq.headers['content-type'].includes('application/json'))
      ) {
        this.fuzzingReq.content = this.parseToJSON(this.fuzzingReq.content)
      }

      const removeFields = ['response', 'id', 'timestamp', 'statusCode']
      removeFields.forEach(e => delete this.fuzzingReq[e])
    },
    sendModifiedRequest() {
      // performs a deep copy
      const item = JSON.parse(JSON.stringify(this.fuzzingReq))

      if ((this.fuzzingReq.headers['Content-Type'] &&
        this.fuzzingReq.headers['Content-Type'].includes('application/json')) ||
        (this.fuzzingReq.headers['content-type'] &&
          this.fuzzingReq.headers['content-type'].includes('application/json'))
      ) {
        item.content = JSON.stringify(this.fuzzingReq.content)
      }
      item.headers = JSON.stringify(this.fuzzingReq.headers)

      if (this.fuzzingReq.modified) {
        item.modified = 1
      } else {
        item.modified = 0
      }

      Object.keys(item).forEach(function(key) {
        if (typeof item[key] === 'string') {
          item[key].replace(/"/g, '\\"')
        }
      })

      modifyRequest(item).then(response => {
        this.$message({
          message: 'Your request has been sent! Response Code: ' + response.status,
          type: 'success'
        })
        this.showModal = false
        this.closeFuzzingModal()
        this.fetchData()
      })
        .catch(error => {
          this.$message.error(error.toString())
        })
    },
    createHeaderButtonClicked() {
      if (this.newHeaderKey !== '') {
        const obj = {}
        obj[this.newHeaderKey] = this.newHeaderValue
        Object.assign(obj, this.fuzzingReq.headers)
        this.fuzzingReq.headers = obj
      }

      this.newHeaderValue = ''
      this.newHeaderKey = ''
      this.isCreatingHeader = false
    },
    createContentButtonClicked() {
      if (this.newContentKey !== '') {
        const obj = {}
        obj[this.newContentKey] = this.newContentValue
        Object.assign(obj, this.fuzzingReq.content)
        this.fuzzingReq.content = obj
      }

      this.newContentValue = ''
      this.newContentKey = ''
      this.isCreatingContent = false
    },
    closeFuzzingModal() {
      this.originalMethodBeforeFuzzing = ''
      this.isEditingFuzzingUrl = false
      this.isEditingFuzzingHeaders = false
      this.isEditingFuzzingContent = false
      this.isEditingFuzzingContentString = false
      this.isCreatingHeader = false
      this.isCreatingContent = false
      this.newHeaderKey = ''
      this.newHeaderValue = ''
      this.newContentKey = ''
      this.newContentValue = ''
    },
    parseToJSON(str) {
      try {
        return JSON.parse(str)
      } catch (error) {
        console.log(error.toString())
        return str
      }
    },
    convertFromEpoch(param) {
      if (param === '' || param === undefined || param === null) {
        return null
      } else {
        const date = new Date(param)
        return date.toLocaleString()
      }
    }
  }
}
</script>

<style scoped>
  .table {
    width: 100%;
    background:#fff;
    color:#525252;
    font-size:14px;
    font-family:Consolas,Menlo,Courier,monospace;
  }

  .fuzzingModal {
    width: 100%;
    background: #fff;
    color: #525252;
    font-size: 14px;
  }

  .warning {
    color: red;
  }

  .bold {
    font-weight: bold;
  }

  .tableRow {
    margin-bottom: 10px;
  }

  .fuzzingRow {
    margin-bottom: 0px;
  }
</style>
