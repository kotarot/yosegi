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

package jp.co.yahoo.yosegi.util.io.diffencoder;

import jp.co.yahoo.yosegi.inmemory.IDictionary;
import jp.co.yahoo.yosegi.inmemory.IMemoryAllocator;
import jp.co.yahoo.yosegi.message.objects.LongObj;
import jp.co.yahoo.yosegi.message.objects.PrimitiveObject;
import jp.co.yahoo.yosegi.util.io.IReadSupporter;
import jp.co.yahoo.yosegi.util.io.IWriteSupporter;
import jp.co.yahoo.yosegi.util.io.NumberToBinaryUtils;

import java.io.IOException;
import java.nio.ByteOrder;

public class DiffLongNumEncoder implements INumEncoder {

  private final long min;
  private final NumberToBinaryUtils.ILongConverter converter;

  /**
   * Check range of diff and initialize.
   */
  public DiffLongNumEncoder( final long min , final long max ) throws IOException {
    if ( min < 0 && ( min + Long.MAX_VALUE ) < max ) {
      throw new IOException( "diff is out of range." );
    }
    this.min = min;
    converter = NumberToBinaryUtils.getLongConverter( 0 , max - min );
  }

  @Override
  public int calcBinarySize( final int rows ) {
    return converter.calcBinarySize( rows );
  }

  @Override
  public void toBinary(
      final long[] longArray ,
      final byte[] buffer ,
      final int start ,
      final int rows ,
      final ByteOrder order ) throws IOException {
    IWriteSupporter wrapBuffer =
        converter.toWriteSuppoter( rows , buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < rows ; i++ ) {
      wrapBuffer.putLong( longArray[i] - min );
    }
  }

  @Override
  public void toBinary(
      final Long[] longArray ,
      final byte[] buffer ,
      final int start ,
      final int rows ,
      final ByteOrder order ) throws IOException {
    IWriteSupporter wrapBuffer =
        converter.toWriteSuppoter( rows , buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < rows ; i++ ) {
      wrapBuffer.putLong( longArray[i].longValue() - min );
    }
  }

  @Override
  public PrimitiveObject[] toPrimitiveArray(
      final byte[] buffer,
      final int start,
      final int rows,
      final ByteOrder order ) throws IOException {
    PrimitiveObject[] result = new PrimitiveObject[rows];
    IReadSupporter wrapBuffer =
        converter.toReadSupporter( buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < rows ; i++ ) {
      result[i] = new LongObj( wrapBuffer.getLong() + min );
    }
    return result;
  }

  @Override
  public PrimitiveObject[] getPrimitiveArray(
      final byte[] buffer ,
      final int start ,
      final int rows ,
      final boolean[] isNullArray ,
      final ByteOrder order ) throws IOException {
    PrimitiveObject[] result = new PrimitiveObject[isNullArray.length];
    IReadSupporter wrapBuffer =
        converter.toReadSupporter( buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < isNullArray.length ; i++ ) {
      if ( ! isNullArray[i] ) {
        result[i] = new LongObj( wrapBuffer.getLong() + min );
      }
    }
    return result;
  }

  @Override
  public void setDictionary(
      final byte[] buffer ,
      final int start ,
      final int rows ,
      final ByteOrder order ,
      final IDictionary dic ) throws IOException {
    IReadSupporter wrapBuffer =
        converter.toReadSupporter( buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < rows ; i++ ) {
      dic.setLong( i , wrapBuffer.getLong() + min );
    }
  }

  @Override
  public void loadInMemoryStorage(
      final byte[] buffer ,
      final int start ,
      final int rows ,
      final boolean[] isNullArray ,
      final ByteOrder order ,
      final IMemoryAllocator allocator ,
      final int startIndex ) throws IOException  {
    IReadSupporter wrapBuffer =
        converter.toReadSupporter( buffer , start , calcBinarySize( rows ) );
    for ( int i = 0 ; i < isNullArray.length ; i++ ) {
      if ( isNullArray[i] ) {
        allocator.setNull( i + startIndex );
      } else {
        allocator.setLong( i + startIndex , wrapBuffer.getLong() + min );
      }
    }
  }

}
