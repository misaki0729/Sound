/*
 * 2014/11/20 作成
 * 音の周波数と名前を保存する
 */
package lego;

public class Scale {
	String scaleName; //音階の名前
	int freq; //周波数
	int freq_asm;

	public Scale(String name, int freq, int freq_asm) {
		scaleName = name;
		this.freq = freq;
		this.freq_asm = freq_asm;
	}
}
