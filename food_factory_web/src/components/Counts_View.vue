<template>
    <div class="counts-view">
        <h1>称重显示</h1>
        <div class="View">
            <!-- <div class="camera_Count">
                <span>检测到的摄像头数量: {{ cameraCount }}</span>
            </div> -->
            <div class="season_View">
                <span>当前测量</span>
                <span>{{ current_seasoning }}</span>
            </div>
            <div class="yolo-result">
                <h2>YOLO 检测结果</h2>
                    <ul>
                    <li v-for="(result, index) in yoloResults" :key="index">
                        类别: {{ result.className }} - 置信度: {{ result.confidence }} - 框: [{{ result.box.join(', ') }}]
                    </li>
                </ul>
            </div>  
            <div class="weight_View">
                <el-input
                    ref="inputRef"
                    v-model="weight"
                    style="max-width: 400px;"
                    @input="handleInput"
                    @blur="focusInput"
                >
                    <template #append>KG</template>
                </el-input>
                <span>{{ show_weight }}</span>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, watch, inject } from 'vue';
import axios from 'axios';

export default {
    props: ['takePhoto_1s'],
    data() {
        return {
            count: 0,
            weight: '', // 用户输入的原始值
            current_seasoning: '',
            current_weight: '',
            show_weight: '',
            ocr_1s:'',
            photoSrcs: [], // 存储两张照片的 base64 数据
            inputBuffer: '', // 输入缓冲区
            timeoutId: null, // 超时 ID，用于控制时间间隔
            cameraCount: 0, // 摄像头数量
            videoStreams: [], // 存储多个摄像头的视频流
            // takePhoto_1s: false, // 控制是否每秒拍照
            singleCameraTimer: null, // 单摄像头定时器
            ChangetakePhoto_1s: false,
            inputNumber: null,
            cameras: [], // 存储摄像头信息
            videoStreams: [], // 存储每个摄像头的视频流对象
            videoElements: [], // 存储每个摄像头的video元素
            imageSrc: [], // 存储每个摄像头拍摄的照片
            storageUrls: [], // 存储每个摄像头照片的服务器地址
            yoloResults: [], // 新增：存储 YOLO 检测结果
        };
    },
    created() {
        this.request = inject('request');
        this.ocrRequest = inject('ocrRequest');
        this.yoloRequest = inject('yoloRequest');
    },
    watch: {
        takePhoto_1s(newValue) {
            if (newValue) {
                this.startSingleCameraCapture(0);
            } else {
                this.stopSingleCameraCapture();
            }
        },
    },
    mounted() {
        // 在组件挂载后立即聚焦到输入框
        this.$nextTick(() => {
            this.focusInput();
        });
        // 获取摄像头设备数量
        this.getCameraCount();
        this.enumerateDevices();
    },
    methods: {
        async getCameraCount() {
            try {
                const devices = await navigator.mediaDevices.enumerateDevices();
                console.log("检测到的所有设备列表:", devices); // 打印所有设备列表
                const videoDevices = devices.filter(device => device.kind === 'videoinput');
                console.log("检测到的摄像头设备列表:", videoDevices); // 打印摄像头设备列表
                this.cameraCount = videoDevices.length;
                this.videoStreams = [];
            } catch (error) {
                console.error("无法获取摄像头设备信息:", error);
            }
        },
        async enumerateDevices() {
            const devices = await navigator.mediaDevices.enumerateDevices();
            this.cameras = devices.filter(device => device.kind === 'videoinput');
        },
        handleInput(value) {
            if (this.timeoutId) {
                clearTimeout(this.timeoutId);
            }
            console.log("value: " + value);

            const filteredValue = value.replace(/[^0-9.]/g, '');
            const validValue = filteredValue.replace(/^(\d*\.\d*).*/, '$1');
            console.log("validValue: " + validValue);
            this.inputBuffer = validValue;

            if (validValue && !isNaN(validValue)) {
                this.timeoutId = setTimeout(() => {
                    this.weight = this.inputBuffer;
                    this.current_weight = parseFloat(this.inputBuffer).toFixed(2);
                    console.log(this.current_weight);
                    this.show_weight = this.current_weight + ' KG';
                    this.weight = '';
                    this.inputBuffer = '';
                    // this.takePhotos(); // 同时拍摄两张照片
                    this.startCamerasAndTakePhotos();
                }, 200);
            } else {
                this.current_weight = '';
                this.show_weight = '';
            }
        },
        startCamerasAndTakePhotos() {
        this.cameras.forEach((camera, index) => {
        
          this.startCamera(index);
        });
      },
      async startCamera(cameraId) {
        try {
          // 动态获取设备列表
          const devices = await navigator.mediaDevices.enumerateDevices();
          const targetCamera = devices.find(device =>
              device.kind === 'videoinput' && device.deviceId === this.cameras[cameraId]?.deviceId
          );

          if (!targetCamera) {
            console.log(`没有找到ID为${cameraId}的摄像头`);
            return;
          }

          // 先尝试不设置约束条件
          let constraints = { video: true };

          try {
            const stream = await navigator.mediaDevices.getUserMedia(constraints);
            this.videoStreams[cameraId] = stream;
            this.playVideo(cameraId);
            setTimeout(() => this.takePhoto(cameraId), 1000);
          } catch (initialError) {
            if (initialError.name === 'OverconstrainedError') {
              // 获取设备支持的约束条件
              const supportedConstraints = navigator.mediaDevices.getSupportedConstraints();
              constraints = {
                video: {
                  deviceId: { exact: targetCamera.deviceId },
                  // 根据支持的约束条件设置合理的值
                  width: supportedConstraints.width ? { ideal: 640 } : undefined,
                  height: supportedConstraints.height ? { ideal: 480 } : undefined
                }
              };

              try {
                const stream = await navigator.mediaDevices.getUserMedia(constraints);
                this.videoStreams[cameraId] = stream;
                this.playVideo(cameraId);
                setTimeout(() => this.takePhoto(cameraId), 1000);
              } catch (adjustedError) {
                console.error(`调整约束条件后访问 ID 为 ${cameraId} 的摄像头时出错:`, adjustedError);
              }
            } else {
              console.error(`访问 ID 为 ${cameraId} 的摄像头时出错:`, initialError);
            }
          }
        } catch (error) {
          console.error(`枚举 ID 为 ${cameraId} 的摄像头设备时出错:`, error);
        }
      },
      playVideo(cameraId) {
        const videoElement = document.createElement('video');
        videoElement.srcObject = this.videoStreams[cameraId];
        videoElement.autoplay = true;
        videoElement.id = `video-${cameraId}`;
        videoElement.style.display = 'none'; // 隐藏视频元素，仅用于捕获图像
        document.body.appendChild(videoElement);
        this.videoElements[cameraId] = videoElement;
      },
      takePhoto(cameraId) {
        
        const videoElement = this.videoElements[cameraId];
        if (videoElement.paused) {
            videoElement.play(); // 确保视频已播放
        }
        const canvas = document.createElement('canvas');
        canvas.width = videoElement.videoWidth;
        canvas.height = videoElement.videoHeight;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(videoElement, 0, 0);
        const dataUrl = canvas.toDataURL('image/png');
        this.imageSrc[cameraId] = dataUrl;
        console.log("imageSrc: " + this.imageSrc[cameraId]);
        this.$emit('photo-taken', dataUrl);
        this.uploadPhoto(dataUrl, cameraId);
      },
      uploadPhoto(dataUrl, cameraId) {
        // const blob = this.dataURItoBlob(dataUrl);
        const formData = new FormData();
        // formData.append('file', blob, `photo_${cameraId}.png`);

        formData.append('file', this.dataURLtoFile(dataUrl, `photo_${cameraId}.png`));
        console.log("formData: " + formData);
        // axios.post('YOUR_SERVER_ENDPOINT', formData, {
        //   headers: {
        //     'Content-Type': 'multipart/form-data'
        //   }
        // }).then(response => {
        //   this.storageUrls[cameraId] = response.data.url; // 假设服务器返回包含存储地址的对象
        // }).catch(error => {
        //   console.error("Error uploading photo:", error);
        // });
        if (cameraId === 0) {
             this.ocrRequest.post('/ocr/upload', formData)
            .then(response => {
                console.log('Success:', response.data);
                this.current_seasoning = response.data;
                console.log("this.current_seasoning"+this.current_seasoning);
                this.$emit('current_seasoning', this.current_seasoning);
                this.$emit('current_weight', this.current_weight);
                // 调用YOLO接口
                return this.yoloRequest.post('/yolo/upload', formData);
            })
            .then(yoloResponse => {
                console.log('YOLO Success:', yoloResponse.data);
                this.yoloResults = yoloResponse.data;
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }
       
        // this.ocrRequest.post('/dl/upload', formDataDl)
        //     .then(response => {
        //         console.log('Success:', response.data);
        //         // this.current_seasoning = response.data;
        //     })
        //     .catch(error => {
        //         console.error('Error:', error);
        //     });
      },
      dataURItoBlob(dataURI) {
        const byteString = atob(dataURI.split(',')[1]);
        const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
        const ab = new ArrayBuffer(byteString.length);
        const ia = new Uint8Array(ab);
        for (let i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }
        return new Blob([ab], { type: mimeString });
      },
        startCameras() {
            this.videoStreams = [];
            navigator.mediaDevices.enumerateDevices().then(devices => {
                const videoInputs = devices.filter(device => device.kind === 'videoinput');
                // 仅选择前两个摄像头
                videoInputs.slice(0, 2).forEach((device, index) => {
                    console.log(`Accessing media device ${index}:`, device);
                    console.log("device.deviceId: " + device.deviceId);
                    
                    navigator.mediaDevices.getUserMedia({ video: { deviceId: device.deviceId } })
                        .then(stream => {
                            this.videoStreams.push({
                                stream: stream,
                                videoElement: document.createElement('video')
                            });
                            this.videoStreams[index].videoElement.srcObject = stream;
                            this.videoStreams[index].videoElement.play();
                            this.videoStreams[index].videoElement.onloadedmetadata = () => {
                                this.takePhotoFromStream(index);
                            };
                        })
                        .catch(err => console.error(`Error accessing media device ${index}.`, err));
                });
            });
        },
        stopCameras() {
            this.videoStreams.forEach(videoStream => {
                if (videoStream.stream) {
                    videoStream.stream.getTracks().forEach(track => track.stop());
                }
            });
            this.videoStreams = [];
        },
        takePhotos() {
            this.photoSrcs = []; // 重置照片数组
            if (this.videoStreams.length === 0) {
                this.startCameras();
            } else {
                // 仅对前两个摄像头进行拍照
                this.videoStreams.slice(0, 2).forEach((videoStream, index) => {
                    this.takePhotoFromStream(index);
                });
            }
    },
    takePhotoFromStream(index) {
        console.log(`Taking photo from stream ${index}`);
        
        const video = this.videoStreams[index]?.videoElement; 
        if (!video ||!video.videoWidth ||!video.videoHeight) {
            console.error(`摄像头 ${index} 的视频元素未准备好`);
            return;
        }
        const canvas = document.createElement('canvas');
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
        const photoSrc = canvas.toDataURL('image/png');
    
        this.photoSrcs[index] = photoSrc;
        this.$emit('photo-taken', photoSrc);
        if (this.photoSrcs.length >= 2) {
            this.uploadImages();
        }
    },
    uploadImages() {
        const formData = new FormData();
        const formDataDl = new FormData();
        if (this.photoSrcs.length == 2 ) {
            formData.append('file', this.dataURLtoFile(this.photoSrcs[0], 'photo0.png'));
            formDataDl.append('file', this.dataURLtoFile(this.photoSrcs[1], 'photo1.png'));
        } else {
            console.error("没有照片可以上传");
            return;
        }

        this.ocrRequest.post('/ocr/upload', formData)
            .then(response => {
                console.log('Success:', response.data);
                this.current_seasoning = response.data;
                console.log("this.current_seasoning"+this.current_seasoning);
                this.$emit('current_seasoning', this.current_seasoning);
                this.$emit('current_weight', this.current_weight);
                // 调用YOLO接口
                return this.yoloRequest.post('/yolo/upload', formData);
            })
            .then(yoloResponse => {
                console.log('YOLO Success:', yoloResponse.data);
                this.yoloResults = yoloResponse.data;
            })
            .catch(error => {
                console.error('Error:', error);
            });
        // this.ocrRequest.post('/dl/upload', formDataDl)
        //     .then(response => {
        //         console.log('Success:', response.data);
        //         // this.current_seasoning = response.data;
        //     })
        //     .catch(error => {
        //         console.error('Error:', error);
        //     });

        this.stopCameras(); // 停止摄像头
    },
    dataURLtoFile(dataurl, filename) {
        let arr = dataurl.split(','),
            mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]),
            n = bstr.length,
            u8arr = new Uint8Array(n);

        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }

        return new File([u8arr], filename, { type: mime });
    },
    focusInput() {
        this.$refs.inputRef.focus();
    },
    beforeDestroy() {
        this.stopCameras();
    },
    startSingleCameraCapture(deviceIndex) {
            navigator.mediaDevices.enumerateDevices().then(devices => {
                const videoInputs = devices.filter(device => device.kind === 'videoinput');
                const selectedDevice = videoInputs[deviceIndex];
                if (!selectedDevice) {
                    console.error(`No device found for index ${deviceIndex}`);
                    return;
                }

                navigator.mediaDevices.getUserMedia({ video: { deviceId: selectedDevice.deviceId } })
                    .then(stream => {
                        this.videoStreams = [{
                            stream: stream,
                            videoElement: document.createElement('video')
                        }];
                        this.videoStreams[0].videoElement.srcObject = stream;
                        this.videoStreams[0].videoElement.play();
                        this.singleCameraTimer = setInterval(() => {
                            this.takeSinglePhotoAndUpload();
                        }, 1000); // 每秒拍照并上传
                    })
                    .catch(err => console.error(`Error accessing media device ${deviceIndex}.`, err));
            });
        },
        takeSinglePhotoAndUpload() {
            const canvas = document.createElement('canvas');
            const video = this.videoStreams[0]?.videoElement; // 确保视频元素存在
            if (!video) return;

            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const ctx = canvas.getContext('2d');
            ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
            const photoSrc = canvas.toDataURL('image/png');

            const formData = new FormData();
            formData.append('file', this.dataURLtoFile(photoSrc, 'single_photo.png'));
            console.log("formData: " + formData);
            
            this.ocrRequest.post('/ocr/upload1s', formData)
                .then(res => {
                    console.log("res",res);
                    if(res.data.code==200){
                        console.log('Success:', res.data.data);
                        this.ocr_1s = res.data.data;
                        // this.$emit('ocr_1s', this.ocr_1s);
                        console.log("takePhoto_1s this.ocr_1s"+this.ocr_1s);
                        if (this.ocr_1s == '2000') {  // 识别成功
                            this.$emit('ChangetakePhoto_1s', !this.takePhoto_1s);
                        }
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },
    stopSingleCameraCapture() {
        if (this.singleCameraTimer) {
            clearInterval(this.singleCameraTimer);
            this.singleCameraTimer = null;
        }
        if (this.videoStreams.length > 0) {
            this.videoStreams[0].stream.getTracks().forEach(track => track.stop());
            this.videoStreams = [];
        }
        },
        
    },
}
</script>

<style scoped>
.counts-view {
    width: 100%;
    margin: 0 auto;
    height: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-direction: column;
}
.View {
    height: 80%;
    display: flex;
    align-items: center;
    justify-content: space-around;
    background-color: #e0e0e0;
    width: 100%;
    border-radius: 10px; /* 添加圆角 */
}
.camera_Count {
    font-size: 20px;
    color: green;
    margin-bottom: 20px;
}
.season_View {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-size: 24px;
    width: 40%;
}
.weight_View {
    width: 60%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-size: 50px;
    color: salmon;
}
h1 {
    padding-top: 30px;
    color: #333;
    font-size: 24px;
}
.season_View span:nth-child(2) {
    font-size: 50px;
    color: red;
}
.el-input {
    width: 50%;
}
</style>