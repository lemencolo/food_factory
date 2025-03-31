<template>
    <div class="video-view">
        <h1>物料识别</h1>
        <div class="demo-image__list">
            <div class="demo-image__placeholder">
                <div class="block">
                    <span class="demonstration">OCR图片</span>
                    <el-image
                        :src="photoSrcs[0]"
                        class="large-image"
                        fit="contain"
                        :preview-src-list="[photoSrcs[0]]"
                        :loading="loading[0]"
                        @load="onLoad(0)"
                        @error="onError(0)"
                    >
                        <template #error>
                            <div class="image-slot">
                                <el-icon><icon-picture /></el-icon>
                            </div>
                        </template>
                        <template #placeholder>
                            <div class="image-loading">
                                <el-loading :fullscreen="false" :lock="true"></el-loading>
                            </div>
                        </template>
                    </el-image>
                </div>
            </div>
            <div class="demo-image__placeholder">
                <div class="block">
                    <span class="demonstration">深度学习图片</span>
                    <el-image
                        :src="photoSrcs[1]"
                        class="large-image"
                        fit="contain"
                        :preview-src-list="[photoSrcs[1]]"
                        :loading="loading[1]"
                        @load="onLoad(1)"
                        @error="onError(1)"
                    >
                        <template #error>
                            <div class="image-slot">
                                <el-icon><icon-picture /></el-icon>
                            </div>
                        </template>
                        <template #placeholder>
                            <div class="image-loading">
                                <el-loading :fullscreen="false" :lock="true"></el-loading>
                            </div>
                        </template>
                    </el-image>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { Picture as IconPicture } from '@element-plus/icons-vue'

export default {
    name: 'VideoView',
    components: {
        IconPicture, // 注册 IconPicture 组件
    },
    props: {
        photoSrcs: {
            type: Array,
            default: () => [] // 默认值是一个空数组
        }
    },
    data() {
        return {
            loading: [true, true]
        }
    },
    methods: {
        onLoad(index) {
            this.loading[index] = false
        },
        onError(index) {
            this.loading[index] = false
        }
    }
}
</script>

<style scoped>
.video-view {
    width: 100%;
    margin: 0 auto;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.block {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 80%;
}

.demo-image__list {
    display: flex;
    justify-content: space-around;
    align-items: center;
    flex-direction: row;
    height: 100%;
    padding: 20px;
}

.demo-image__placeholder {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 45%;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 10px;
    transition: all 0.3s ease; /* 添加过渡效果 */
}

.demo-image__placeholder:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* 鼠标悬停时增加阴影 */
    transform: translateY(-5px); /* 鼠标悬停时向上移动 */
}

.demo-image__placeholder .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 80%;
    height: 80%;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
    font-size: 14px;
    border-radius: 8px;
    padding: 20px; /* 增加内边距 */
    text-align: center; /* 文本居中 */
    transition: all 0.3s ease; /* 添加过渡效果 */
}

.demo-image__placeholder .image-slot:hover {
    background: var(--el-fill-color); /* 鼠标悬停时改变背景颜色 */
}

.large-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
}

.image-loading {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
</style>