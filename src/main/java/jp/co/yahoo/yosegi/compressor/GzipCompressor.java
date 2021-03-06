/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.yahoo.yosegi.compressor;

import jp.co.yahoo.yosegi.util.io.InputStreamUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements ICompressor {

  private int getCompressLevel( final CompressionPolicy compressionPolicy ) {
    switch ( compressionPolicy ) {
      case BEST_SPEED:
        return Deflater.BEST_SPEED;
      case SPEED:
        return 6 - 2;
      case DEFAULT:
        return 6;
      case BEST_COMPRESSION:
        return Deflater.BEST_COMPRESSION;
      default:
        return 6;
    }
  }

  @Override
  public byte[] compress(
      final byte[] data ,
      final int start ,
      final int length ,
      final CompressResult compressResult ) throws IOException {
    int level = getCompressLevel( compressResult.getCompressionPolicy() ); 
    int optLevel = compressResult.getCurrentLevel();
    if ( ( level - optLevel ) < 1 ) {
      compressResult.setEnd();
      optLevel = compressResult.getCurrentLevel();
    }

    int setLevel = level - optLevel;
    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
    GZIPOutputStream out = new GZIPOutputStream( byteArrayOut ) {
      {
        this.def.setLevel( setLevel );
      }
    };

    out.write( data , start , length );
    out.flush();
    out.finish();
    byte[] compressByte = byteArrayOut.toByteArray();
    byte[] retVal = new byte[ Integer.BYTES + compressByte.length ];
    ByteBuffer wrapBuffer = ByteBuffer.wrap( retVal );
    wrapBuffer.putInt( length );
    wrapBuffer.put( compressByte );

    byteArrayOut.close();
    out.close();

    compressResult.feedBack( length , compressByte.length );

    return retVal;
  }

  @Override
  public int getDecompressSize(
      final byte[] data , final int start , final int length ) throws IOException {
    ByteBuffer wrapBuffer = ByteBuffer.wrap( data , start , length );
    return wrapBuffer.getInt();
  }

  @Override
  public byte[] decompress(
      final byte[] data , final int start , final int length ) throws IOException {
    ByteBuffer wrapBuffer = ByteBuffer.wrap( data , start , length );
    int dataLength = wrapBuffer.getInt();

    ByteArrayInputStream byteArrayIn =
        new ByteArrayInputStream( data , start + Integer.BYTES , length );
    GZIPInputStream in = new GZIPInputStream( byteArrayIn , 1024 * 256 );

    byte[] retVal = new byte[dataLength];
    InputStreamUtils.read( in , retVal , 0 , dataLength );

    return retVal;
  }

  @Override
  public int decompressAndSet(
      final byte[] data ,
      final int start ,
      final int length ,
      final byte[] buffer ) throws IOException {
    ByteBuffer wrapBuffer = ByteBuffer.wrap( data , start , length );
    int dataLength = wrapBuffer.getInt();

    ByteArrayInputStream byteArrayIn =
        new ByteArrayInputStream( data , start + Integer.BYTES , length );
    GZIPInputStream in = new GZIPInputStream( byteArrayIn , 1024 * 256 );

    InputStreamUtils.read( in , buffer , 0 , dataLength );

    return dataLength;
  }

}
