package com.tohier.cartercoin.zxing.camera;

public interface PreviewFrameShotListener {
	public void onPreviewFrame(byte[] data, Size frameSize);
}
