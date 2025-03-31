# from paddleocr import PaddleOCR, draw_ocr

# # Paddleocr目前支持的多语言语种可以通过修改lang参数进行切换
# # 例如`ch`, `en`, `fr`, `german`, `korean`, `japan`
# ocr = PaddleOCR(use_angle_cls=True, lang="ch")  # need to run only once to download and load model into memory
# img_path = './4.png'
# result = ocr.ocr(img_path, cls=True)
# for idx in range(len(result)):
#     res = result[idx]
#     for line in res:
#         print(line)

# # 显示结果
# from PIL import Image
# result = result[0]
# image = Image.open(img_path).convert('RGB')
# boxes = [line[0] for line in result]
# txts = [line[1][0] for line in result]
# scores = [line[1][1] for line in result]
# im_show = draw_ocr(image, boxes, txts, scores, font_path='./fonts/simfang.ttf')
# im_show = Image.fromarray(im_show)
# im_show.save('result.jpg')

import sys
from paddleocr import PaddleOCR, draw_ocr
from PIL import Image
import os

def main(img_path):
    # 初始化PaddleOCR
    ocr = PaddleOCR(use_angle_cls=True, lang="ch",rec_model_dir="./weight/ch_PP-OCRv4_rec_infer/", det_model_dir="./weight/ch_PP-OCRv4_det_infer/",use_gpu=True)  # 需要运行一次以下载并加载模型到内存中
    
    # 执行OCR识别
    result = ocr.ocr(img_path, cls=True)
    # print(result)
    # 提取文本结果
    extracted_texts = []
    for idx in range(len(result)):
        res = result[idx]
        # print(res)
        for line in res:
            text = line[1][0]  # 获取识别出的文字
            # print(text)
            extracted_texts.append(text)

    # 返回提取的文本结果
    return "\n".join(extracted_texts)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py <image_path>")
        sys.exit(1)

    img_path = sys.argv[1]
    result = main(img_path)
    # print(result)