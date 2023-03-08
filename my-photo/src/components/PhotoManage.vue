<template>
  <div class="PhotoManage">
    <el-form ref="form" :model="sizeForm" label-width="150px" label-position="right" :inline="true">
      <el-form-item label="文件地点（省）">
        <el-select
          v-model="sizeForm.admdvsProvPt"
          clearable placeholder="请选择"
          @change="selectAdmdvsProv">
          <el-option
            v-for="item in admdvsList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件夹（省+文件夹）">
        <el-cascader
          v-model="sizeForm.admdvs"
          :options="admdvsList"
          clearable
          @change="handleChange"></el-cascader>
      </el-form-item>
      <el-form-item label="文件创建时间">
        <el-date-picker
          v-model="sizeForm.selectDate"
          type="daterange"
          align="right"
          unlink-panels
          value-format="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="dialogVisible = true">新增文件</el-button>
        <el-button type="primary" @click="fnQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <el-tabs v-model="activeName" @tab-click="fnChangeActiveName">
      <el-tab-pane label="文件管理" name="first">
        <div style="margin-top: 20px">
          <el-button plain @click="toggleSelection()">取消选择</el-button>
          <el-button
            type="danger"
            plain
            :disabled="this.multipleSelection.length <= 0"
            @click="toggleDeleteSelection()">删除所选项</el-button>
        </div>
        <el-table
          :data="tableData"
          ref="multipleTable"
          height="500"
          stripe
          max-height="500"
          @selection-change="handleSelectionChange"
          style="width: 100%">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            prop="admdvsProv"
            label="文件所在省份"
            fixed
            width="200">
          </el-table-column>
          <el-table-column
            prop="admdvsCity"
            label="文件夹"
            fixed
            width="200">
          </el-table-column>
          <el-table-column
            label="文件预览（图片）"
            fixed
            width="150">
            <template slot-scope="scope">
              <el-image
                style="width: 100px;"
                :src="scope.row.filePath"
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column
            prop="fileName"
            label="文件名称"
            width="300">
          </el-table-column>
          <el-table-column
            prop="fileSize"
            label="文件大小（MB）"
            width="130">
          </el-table-column>
          <el-table-column
            prop="fileType"
            label="文件类型"
            width="120">
          </el-table-column>
          <el-table-column
            prop="admdvsPt"
            label="文件夹拼音"
            width="350">
          </el-table-column>
          <el-table-column
            prop="fileRemarks"
            label="文件说明"
            width="180">
          </el-table-column>
          <el-table-column
            prop="updateTime"
            label="文件修改时间"
            width="180">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="文件创建时间"
            width="180">
          </el-table-column>
          <el-table-column
            prop="userName"
            label="上传修改人员"
            width="120">
          </el-table-column>
          <el-table-column
            prop="fileId"
            label="文件主键"
            width="180">
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="primary" size="mini" icon="el-icon-edit" circle></el-button>
              <el-button @click="handleClickDelete(scope.row)" type="danger" size="mini" icon="el-icon-delete" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[25, 50, 100, 200]"
          :page-size="sizePage"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableDataTotal">
        </el-pagination>
      </el-tab-pane>
      <el-tab-pane label="回收站" name="second">
        回收站
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      title="新增文件"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-radio-group v-model="labelPosition" size="small">
        <el-radio-button label="1">上传至原有文件夹</el-radio-button>
        <el-radio-button label="2">新建文件夹</el-radio-button>
      </el-radio-group>
      <el-form :model="uploadForm" ref="uploadForm"  label-width="80px" label-position="right">
        <el-form-item label="文件地点">
          <el-cascader
            v-model="uploadForm.admdvs"
            :options="admdvsList"
            :disabled="labelPosition == '2'"
            clearable
            @change="handleChange2"></el-cascader>
        </el-form-item>
        <el-form-item label="所在省">
          <el-input v-model="uploadForm.admdvsProv" :disabled="labelPosition == '1'"></el-input>
        </el-form-item>
        <el-form-item label="文件夹">
          <el-input v-model="uploadForm.admdvsName" :disabled="labelPosition == '1'"></el-input>
        </el-form-item>
      </el-form>
      <el-upload
        class="upload-demo"
        ref="upload"
        action="http://127.0.0.1:8999/fileManageController/upload/"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        :on-success="fnUploadSuccess"
        :on-error="fnUploadError"
        :auto-upload="false"
        :data="this.uploadForm"
        :disabled="labelPosition == '2'"
        :multiple="true"
        list-type="picture">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <div slot="tip" class="el-upload__tip">请在填写/选择信息并选取文件完毕后点击【确定】上传，请勿上传大于5M的文件！</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="fnAddAdmdvs" :disabled="labelPosition == '1'">上传位置信息</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="fnUpload" :disabled="labelPosition == '2'">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PhotoManage',
  data () {
    return {
      // 查询表单对象
      sizeForm: {
        admdvs: [],
        admdvsProvPt: "",
        admdvsName: "",
        selectDate: [],
        tar: "",
      },
      // 上传文件的对象
      uploadForm: {
        admdvs: "",
        admdvsProv: "",
        admdvsName: ""
      },
      // 存储区划的列
      admdvsList: [],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      dialogVisible: false, // 相册新增展示框
      labelPosition: "1", // 选择区划为手动录入还是选择，1：选择；2：手动录入
      fileList: [],
      // 存储表格数据
      tableData: [],
      activeName: "first",
      // 当前页
      currentPage: 1,
      // 存储每页条数
      sizePage: 50,
      // 存储数据数量
      tableDataTotal: 0,
      // 被选中的行
      multipleSelection: []
    }
  },
  mounted() {
    this.fnGetAdmdvsList();
  },
  methods: {
    fnGetAdmdvsList() {
      this.$http.get("/api/commonController/queryAdmdvsList").then((data) => {
        this.admdvsList = data.body.data
      });
    },
    handleChange(value) {
      this.sizeForm.admdvsProvPt = null;
      console.log(value);
    },
    handleChange2(value) {
      console.log(value);
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        }).catch(_ => {});
    },
    fnQuery() {
      const data = Object.assign(this.sizeForm, {
        pageNum: this.currentPage,
        pageSize: this.sizePage
      });
      if (data.admdvs != null && data.admdvs != "") {
        for (let i = 0; i < data.admdvs.length; i++) {
          if (i == 0) {
            data.admdvsPt = data.admdvs[i];
          } else {
            data.admdvsPt += data.admdvs[i];
          }
        }
      }
      this.$http.post("/api/fileManageController/queryFileOutList", data)
        .then((res) => {
          const resData = res.body.data;
          this.tableDataTotal = resData.total
          this.tableData = resData.list
      }).catch((err) => {
        console.log(err)
      })
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    fnUpload() {
      if (this.uploadForm.admdvs == "" || this.uploadForm.admdvs == null) {
        this.$message.error("请选择正确的地理位置！");
        return;
      }
      console.log(this.fileList)
      this.$refs.upload.submit();
    },
    fnUploadSuccess(response, file, fileList) {
      // 不知道为什么删除不了表单。。。所以先这样写吧
      this.uploadForm = { admdvs: "", admdvsProv: "", admdvsName: "" }
      this.$refs.upload.clearFiles();
      this.dialogVisible = false;
      // 重新获取一回区划，防止出现新增的问题
      this.fnGetAdmdvsList();
      this.$message({
        showClose: true,
        message: response.data,
        type: 'success'
      });
    },
    fnUploadError(err, file, fileList) {
      console.log("失败！");
      this.$message.error(err);
      console.log(err);
    },
    // 点击每列的修改按钮
    handleClick(row) {
      console.log(row);
    },
    // 点击每列的删除按钮
    handleClickDelete(row) {
      this.$confirm('此操作文件将会进入回收站,并于30天后自动删除,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.fnDeleteFileByOne(row)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 删除/还原单个文件
    fnDeleteFileByOne(value) {
      let data = {
        fileId: value.fileId,
        fileDeleteFlag: this.activeName == "first" ? "0" : "1"
      }
      console.log(data)
      this.$http.post("/api/fileManageController/updateFileDeleteFlagByFileId", data)
        .then(res => {
        if (res.body.code != "200") {
          this.$message.error(res.body.errorMsg)
        } else {
          this.$message.success(res.body.data)
          this.fnQuery()
        }
      })
    },
    fnChangeActiveName(tab, event) {
      console.log(tab, event);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      console.log(this.sizePage)
      this.fnQuery()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      console.log(this.currentPage)
      this.fnQuery()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(this.multipleSelection)
    },
    // 取消选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    toggleDeleteSelection() {
      console.log(this.multipleSelection)
    },
    selectAdmdvsProv(value) {
      this.sizeForm.admdvs = null;
      this.sizeForm.admdvsPt = null;
    },
    // 上传位置信息
    fnAddAdmdvs() {
      const data = this.uploadForm
      if (data.admdvsProv == "" || data.admdvsProv == null) {
        this.$message.error("请填写正确的省！");
        return;
      }
      if (data.admdvsName == "" || data.admdvsName == null) {
        this.$message.error("请填写正确的文件夹！");
        return;
      }
      this.$http.post("/api/fileManageController/addAdmdvs", data)
        .then((res) => {
          this.$message.success(res.body.data);
          this.fnGetAdmdvsList();
      }).catch(err => {})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-form {
  margin-top: 20px;
  margin-bottom: 10px;
}
</style>
