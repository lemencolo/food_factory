from ultralytics import YOLO

# Load a COCO-pretrained YOLO11n model
model = YOLO("best.pt")

# Run inference with the YOLO11n model on the 'bus.jpg' image
results = model(source=0, show=True)