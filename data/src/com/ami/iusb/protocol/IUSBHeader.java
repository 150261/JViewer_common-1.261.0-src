/****************************************************************
 **                                                            **
 **    (C) Copyright 2006-2009, American Megatrends Inc.       **
 **                                                            **
 **            All Rights Reserved.                            **
 **                                                            **
 **        5555 Oakbrook Pkwy Suite 200, Norcross,             **
 **                                                            **
 **        Georgia - 30093, USA. Phone-(770)-246-8600          **
 **                                                            **
****************************************************************/
/*
 * IUSBHeader.java
 *
 * Created on January 14, 2005, 3:52 PM
 */

package com.ami.iusb.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.ami.iusb.RedirProtocolException;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.LocaleStrings;

final public class IUSBHeader extends RedirHeader
{
    public String signature;
    public int major;
    public int minor;
    public int packetHeaderLen;
    public int headerChecksum;
    public long dataPacketLen;
    public int serverCaps;
    public int deviceType;
    public int protocol;
    public int direction;
    public int deviceNumber;
    public int interfaceNumber;
    public int clientData ;
    public int Instance ;
    public long sequenceNumber;
    public byte[] reserved = new byte[ 4 ];
    public static final int HEADER_LEN = 32;
    private static final int IUSB_MAJOR = 1;
    private static final int IUSB_MINOR = 0;

   /**
    *  Creates an empty IUSBHeader
    *
    */
    public IUSBHeader()
    {
        headerLen = HEADER_LEN;
    }

   /**
    * Create an IUSBHeader pre-formatted for cdrom packets
    * @param dataLen
    * @return
    */
    public static IUSBHeader createCDROMHeader( int dataLen )
    {
        IUSBHeader header = new IUSBHeader();
        header.signature = "IUSB    ";
        header.packetHeaderLen = HEADER_LEN;
        header.major = IUSB_MAJOR;
        header.minor = IUSB_MINOR;
        header.sequenceNumber = 0; //FIXME...or not.  Seems to be ignored.
        header.direction = 0x80;
        header.dataPacketLen = dataLen;
        header.deviceType = 0x05;
        header.deviceNumber = 0;
        header.interfaceNumber = 0;
        header.protocol = 0x01;
        return( header );
    }

    /**
     * Received request SCSI packet parsing the data fro the request buffer
     */
    public void read( ByteBuffer rawBuffer ) throws RedirProtocolException, BufferUnderflowException
    {
        byte[] sigTemp = new byte[ 8 ];
        rawBuffer.get( sigTemp );
        signature = new String( sigTemp );
        if( !signature.equals( "IUSB    ") ) {
			Debug.out.dump(sigTemp);
            throw new RedirProtocolException(LocaleStrings.getString("7_1_IUSBH"));
		}

        major = ( (int)rawBuffer.get() ) & 0xff;
        minor = ( (int)rawBuffer.get() ) & 0xff;
        packetHeaderLen = ( (int)rawBuffer.get() ) & 0xff;
        headerChecksum = ( (int)rawBuffer.get() ) & 0xff;
        dataPacketLen = ( (long)rawBuffer.getInt() ) & 0xffffffff;
        serverCaps = ( (int)rawBuffer.get() ) & 0xff;
        deviceType = ( (int)rawBuffer.get() ) & 0xff;
        protocol = ( (int)rawBuffer.get() ) & 0xff;
        direction = ( (int)rawBuffer.get() ) & 0xff;
        deviceNumber = ( (int)rawBuffer.get() ) & 0xff;
        interfaceNumber = ( (int)rawBuffer.get() ) & 0xff;
        clientData =  ( (int)rawBuffer.get() ) & 0xff;
        Instance  = ( (int)rawBuffer.get() ) & 0xff;
        sequenceNumber = ( (long)rawBuffer.getInt() ) & 0xffffffff;
        rawBuffer.get( reserved );

    }

    /**
     * Sending the SCSI packet to the HOST Forming and
     *  filling the SCSI packet with needed information
     */
    public void write( ByteBuffer rawBuffer )
    {
        rawBuffer.put( signature.getBytes() );
        rawBuffer.put( (byte)( major & 0xff ) );
        rawBuffer.put( (byte)( minor & 0xff ) );
        rawBuffer.put( (byte)( packetHeaderLen & 0xff ) );
        rawBuffer.put( (byte)( headerChecksum & 0xff ) );
        rawBuffer.putInt( (int)( dataPacketLen & 0xffffffff ) );
        rawBuffer.put( (byte)( serverCaps & 0xff ) );
        rawBuffer.put( (byte)( deviceType & 0xff ) );
        rawBuffer.put( (byte)( protocol & 0xff ) );
        rawBuffer.put( (byte)( direction & 0xff ) );
        rawBuffer.put( (byte)( deviceNumber & 0xff ) );
        rawBuffer.put( (byte)( interfaceNumber & 0xff ) );
        rawBuffer.put( (byte)( clientData & 0xff ) );
        rawBuffer.put( (byte)( Instance & 0xff ) );
        rawBuffer.putInt( (int)( sequenceNumber & 0xffffffff ) );
        rawBuffer.put( reserved );

        /* Calculate modulo100 checksum */
        int temp = 0;
        for( int i = 0; i < rawBuffer.limit(); i++ )
            temp = ( temp + (int)( rawBuffer.get( i ) & 0xff ) ) & 0xff;

        /* Plug the checksum into the header */
        rawBuffer.put( 11, (byte)-( (byte)( temp & 0xff ) ) );
    }

}
