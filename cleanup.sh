#!/bin/bash

echo "[Cleanup] Removing old files from /opt/tailo..."
# /opt/tailo 디렉토리의 모든 파일과 폴더를 삭제
rm -rf /opt/tailo/*

# 필요한 경우 특정 파일만 삭제하고 싶다면, 아래와 같이 구체적으로 지정할 수 있음
# 예: rm -f /opt/tailo/some_file.jar
