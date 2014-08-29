#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define HEADERSIZE 44

int main() {
  FILE *fp, *fc;
  unsigned long quantization;
  unsigned long i;
  unsigned long datasize;
  unsigned char store[HEADERSIZE];
  unsigned short sampling;
  unsigned dataspeed;
  unsigned blocksize;
  unsigned long bitsize;
  unsigned waveform;
  unsigned long loop;
  short i_plus;
  int median = 1;
  if ((fp = fopen("sen_new.wav", "rwb")) == NULL) {
    printf("File open error\n");
    exit(EXIT_FAILURE);
  }
  fc = fopen("sen_new_new.wav", "wb");
  //ビットサイズを読み込み
  fseek(fp, 34, SEEK_SET);
  fread(store, sizeof(unsigned char), 2, fp);
  memcpy(&bitsize, store, 2);
  fseek(fp, 0, SEEK_SET);
  while (1) {
    printf("変換後の量子化ビット数を指定してください(1~16) : ");
    scanf("%ld", &quantization);
    if (quantization > 0 && quantization <= 16 && bitsize != quantization)
      break;
    else
      printf("その値は指定できません\n");
  }
  for (i = 0; i < quantization-1; i++) 
    median *= 2;
  
  //RIFFを読み込み
  fread(store, sizeof(unsigned char), 4, fp);
  fwrite(store, sizeof(unsigned char), 4, fc);
  //データサイズを読み込み
  fread(store, sizeof(unsigned char), 4, fp);
  memcpy(&datasize, store, 4);
  if (bitsize > 8 && quantization <= 8) {
    datasize = datasize/2 + 18;
  } else if (bitsize <= 8 && quantization > 8) {
    datasize = datasize*2 - 36;
  }
  fwrite(&datasize, sizeof(unsigned char), 4, fc);
  //ヘッダを読み込み
  fread(store, sizeof(unsigned char), 20, fp);
  fwrite(store, sizeof(unsigned char), 20, fc);
  //データ速度を読み込み
  fread(store, sizeof(unsigned char), 4, fp);
  memcpy(&dataspeed, store, 4);
  if (bitsize > quantization) {
    dataspeed /= (bitsize/quantization);
  } else if (bitsize < quantization) {
    dataspeed /= (quantization/bitsize);
  }
  fwrite(&dataspeed, sizeof(unsigned char), 4, fc);
  //ブロックサイズを読み込み
  fread(store, sizeof(unsigned char), 2, fp);
  memcpy(&blocksize, store, 2);
  if (bitsize > quantization) {
     blocksize /= (bitsize/quantization);
  } else if (bitsize < quantization) {
    blocksize /= (quantization/bitsize);
  }
  fwrite(&blocksize, sizeof(unsigned char), 2, fc);
  //ビットサイズを読み込み
  fread(store, sizeof(unsigned char), 2, fp);
  fwrite(&quantization, sizeof(unsigned char), 2, fc);
  //dataチャンク
  fread(store, sizeof(unsigned char), 4, fp);
  fwrite(store, sizeof(unsigned char), 4, fc);
  //波形データサイズを読み込み
  fseek(fp, 4, SEEK_CUR);
  datasize -= 36;
  fwrite(&datasize, sizeof(unsigned char), 4, fc);
  //波形書き換え
  if (bitsize > 8 && quantization > 8) {
    loop = datasize;
    i_plus = 2;
  } else if (bitsize > 8 && quantization <= 8) {
    loop = datasize*2;
    i_plus = 2;
  } else if (bitsize <= 8 && quantization > 8) {
    loop = datasize*2;
    i_plus = 1;
  } else {
    loop = datasize;
    i_plus = 1;
  }
  for (i = 0; i < loop; i += i_plus) {
    fread(store, sizeof(unsigned char), 2, fp);
    memcpy(&waveform, store, 2);
    if (bitsize > quantization) {
      waveform = waveform >> (bitsize - quantization);
    } else {
      waveform = waveform << (quantization - bitsize);
    }
    if (waveform < median) {
      waveform += median;
    } else {
      waveform = waveform - median;
    }
    if (quantization <= 8) 
      fwrite(&waveform, sizeof(unsigned char), 1, fc);
    else 
      fwrite(&waveform, sizeof(unsigned char), 2, fc);
  }
  fclose(fp);
  fclose(fc);
  printf("正常に終了しました\n");
  return 0;
}
    
