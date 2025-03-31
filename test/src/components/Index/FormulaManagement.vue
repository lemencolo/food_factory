<template>
  <div>
    <!-- 主表格 -->
    <el-table :data="tableData" style="width: 100%" stripe>
      <el-table-column type="expand">
        <template #default="props">
          <el-table :data="props.row.ingredients" border>
            <el-table-column label="Ingredient Name" prop="ingredientName"></el-table-column>
            <el-table-column label="Weight (g)" prop="weight"></el-table-column>
            <el-table-column label="Order Index" prop="orderIndex"></el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="Flavour Name">
        <template #default="scope">
          {{ scope.row.data }}
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="Operate">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="editRow(scope.row)">
            Edit
          </el-button>
          <el-popconfirm
            confirm-button-text="Yes"
            cancel-button-text="No"
            icon="InfoFilled"
            icon-color="#626AEF"
            title="Are you sure to delete this?"
            @confirm="deleteRow(scope.row)"
          >
            <template #reference>
              <el-button link type="primary" size="small">Delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加按钮 -->
    <el-button type="primary" @click="addNewRow">Add New Flavour</el-button>

    <!-- 编辑/添加对话框 -->
    <el-dialog v-model="dialogFormVisible" :title="dialogTitle" width="50vw">
      <el-form :model="currentRow" ref="formRef" class="demo_form">
        <el-form-item label="Flavour Name" :label-width="formLabelWidth">
          <el-input v-model="currentRow.data" autocomplete="off" />
        </el-form-item>
        <el-form-item v-for="(ingredient, index) in currentRow.ingredients" :key="index" :label="'Ingredient ' + (index + 1)" class="custom-form-item">
          <el-input v-model="ingredient.ingredientName" placeholder="Ingredient Name" style="width: 20%;" />
          <div style="width: 40%;">
            <span>重量(G)</span>
            <el-input v-model.number="ingredient.weight" :min="0" placeholder="Weight (g)" style="width: 60%;">
              <template #append>G</template>
            </el-input>
          </div>
          <div style="width: 20%; display: flex; justify-content: space-between;">
            <span>次序</span>
            <el-input-number v-model.number="ingredient.orderIndex" :min="0" placeholder="Order Index" style="width: 75%;" />
          </div>
          <el-button type="danger" @click="removeIngredient(index)" style="width: 15%; margin-left: 5%;">Remove</el-button>
        </el-form-item>
        <el-form-item style="margin: 0 auto;">
          <el-button type="primary" @click="addIngredient">Add Ingredient</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveRow">Confirm</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  setup() {
    const tableData = ref([]);
    const dialogFormVisible = ref(false);
    const isEditMode = ref(false);
    const currentRow = ref({});
    const formLabelWidth = '120px';

    const dialogTitle = computed(() => {
      return isEditMode.value ? 'Edit Flavour' : 'Add New Flavour';
    });

    // 模拟请求函数
    const request = {
      get(url) {
        return new Promise((resolve) => {
          setTimeout(() => resolve({ code: 200, data: { sortedFlavours: {} } }), 1000);
        });
      },
      put(url, data) {
        return new Promise((resolve) => {
          setTimeout(() => resolve({ code: 200, message: '' }), 1000);
        });
      }
    };

    const editRow = (row) => {
      currentRow.value = JSON.parse(JSON.stringify(row));
      currentRow.value.originalFlavourName = row.data;
      isEditMode.value = true;
      dialogFormVisible.value = true;
    };

    const deleteRow = (row) => {
      console.log(row.data);
      request.get(`/flavourIngredients/delete?name=${encodeURIComponent(row.data)}`).then(response => {
        if (response.code === 200) {
          ElMessage.success('删除成功');
          postFlavourList();
        } else {
          ElMessage.error(response.msg || '删除失败');
        }
      }).catch(error => {
        console.error('删除时发生错误:', error);
        ElMessage.error('服务器请求失败');
      });
    };

    const addNewRow = () => {
      currentRow.value = {
        data: '',
        ingredients: [{ ingredientName: '', weight: 0, orderIndex: 0 }],
      };
      isEditMode.value = false;
      dialogFormVisible.value = true;
    };

    const addIngredient = () => {
      currentRow.value.ingredients.push({ ingredientName: '', weight: 0, orderIndex: 0 });
    };

    const removeIngredient = (index) => {
      if (currentRow.value.ingredients.length > 1) {
        currentRow.value.ingredients.splice(index, 1);
      } else {
        ElMessage.warning('At least one ingredient is required.');
      }
    };

    const saveRow = async () => {
      const flavourData = {
        new_flavour_name: currentRow.value.data,
        ingredients: currentRow.value.ingredients.map(ingredient => ({
          ingredient_name: ingredient.ingredientName,
          weight: Number(ingredient.weight),
          order_index: ingredient.orderIndex,
        })),
      };

      if (isEditMode.value) {
        flavourData['old_flavour_name'] = currentRow.value.originalFlavourName;
      }

      try {
        const response = await request.put('/flavourIngredients/addOrUpdate', flavourData);
        if (response.code === 200) {
          ElMessage.success(isEditMode.value ? '更新成功' : '添加成功');
          dialogFormVisible.value = false;
          postFlavourList();
        } else {
          ElMessage.error(response.message || (isEditMode.value ? '更新失败' : '添加失败'));
        }
      } catch (error) {
        console.error('保存时发生错误:', error);
        ElMessage.error('服务器请求失败');
      }
    };

    const postFlavourList = async () => {
      try {
        const response = await request.get('/flavourIngredients/all-grouped');

        if (response.code === 200) {
          tableData.value = Object.entries(response.data.sortedFlavours).map(([flavourName, ingredients]) => ({
            flavourName,
            ingredients: ingredients,
          }));

          console.log(response);
        } else {
          ElMessage.error(response.message || '获取数据失败');
        }
      } catch (err) {
        console.error('An unexpected error occurred:', err);
      }
    };

    const init = async () => {
      await postFlavourList();
    };

    init();

    return {
      tableData,
      dialogFormVisible,
      isEditMode,
      currentRow,
      formLabelWidth,
      dialogTitle,
      editRow,
      deleteRow,
      addNewRow,
      addIngredient,
      removeIngredient,
      saveRow,
    };
  }
};
</script>

<style scoped>
.demo_form {
  display: flex;
  flex-direction: column;
}
.custom-form-item {
  display: flex;
  align-items: center;
}
.custom-form-item .el-form-item__content {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>


