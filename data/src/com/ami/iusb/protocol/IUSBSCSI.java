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
 * IUSBSCSI.java
 *
 * Created on January 27, 2005, 2:12 PM
 */

package com.ami.iusb.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.ami.iusb.RedirProtocolException;



public final class IUSBSCSI extends RedirPacket
{

    public byte Lba;//For eject Lba is 0x00020000 and for load 0x00030000
    public int instanceNum;//get the instance no display in JViewer
    public int connectionStatus = -1;
    public int dataLen;
    public int opcode;
    public ByteBuffer data;
    public String m_otherIP;
    private boolean preBuffered;

    public static final int IUSB_SCSI_PKT_SIZE = 62; //this is including IUSB_HEADER(of size 32).
    public static final int IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER = 30;

    public static final int OPCODE_EJECT = 0x1b;
    public static final int OPCODE_KILL_REDIR = 0xf6;
    

    /**
     * Getting the USB header from the received Reuwst packet
     * @param header
     */
    public IUSBSCSI( IUSBHeader header )
    {
        this.header = header;
        instanceNum = header.Instance;
        dataLen = (int)header.dataPacketLen;
    }

    /**
     *	Method Reads the data and assigned to the heade data
     * @param packetBuffer
     * @param preBuffered
     * @throws RedirProtocolException
     */
    public IUSBSCSI( ByteBuffer packetBuffer, boolean preBuffered ) throws RedirProtocolException
    {
        this.preBuffered = preBuffered;
        header = new IUSBHeader();
        header.read( packetBuffer );
        ( (IUSBHeader)header ).dataPacketLen = packetBuffer.limit() - header.headerLen;
        dataLen = (int)( (IUSBHeader)header ).dataPacketLen;
        data = packetBuffer.slice();
    }

    /***
     *Creating the packet to send to the CDServer
     */
    public void writePacket( ByteBuffer buffer )
    {
        /* Set direction to be FROM_REMOTE, as we're sending this now */
        ( (IUSBHeader)header ).direction = 0x80;
        /* Write out the header into buffer */
        header.write( buffer );

        if( preBuffered )
        {
            buffer.limit( dataLen + IUSBHeader.HEADER_LEN );
            buffer.position( buffer.limit() );
        }
        else
            buffer.put( data );
    }

    /**
     *Parse the Readed request data
     */
    public void readData( ByteBuffer buffer ) throws BufferUnderflowException
    {
        /* Interpret data */
        byte [] otherIP = new byte[24];
        data = ByteBuffer.allocate( dataLen );

		if( buffer.remaining() < dataLen )
			throw new BufferUnderflowException();

        data.put( buffer );
        Lba = data.get(13);
        opcode = (int)data.get( 9 ) & 0xff;
        if( ( opcode == 0xf1 ) && ( dataLen > 30 ) )
        {
			connectionStatus = (int)data.get( 30 );
			try {
				data.position( 31 );
				data.get(otherIP);
				m_otherIP = new String(otherIP).trim();
			}
			catch(BufferUnderflowException e)
			{
				System.err.println( e.getMessage() );
				m_otherIP = new String(""); /* fill with empty string if any exception occurs */
				throw new BufferUnderflowException();
			}
		}
        else
            connectionStatus = -1;
    }
}
