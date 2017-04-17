package com.xiaoyezi.midicore.factorytool.utils;

import com.xiaoyezi.midicore.factorytool.data.FileModel;

import java.util.Comparator;

class SortByTime implements Comparator<FileModel> {
	public SortByTime() {
	}

	@Override
	public int compare(FileModel lhs, FileModel rhs) {
		return (int)(rhs.getLastModified() - lhs.getLastModified());
	}
}
