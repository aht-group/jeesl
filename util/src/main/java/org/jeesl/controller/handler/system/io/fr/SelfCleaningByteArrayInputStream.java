package org.jeesl.controller.handler.system.io.fr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SelfCleaningByteArrayInputStream extends ByteArrayInputStream
{
    public SelfCleaningByteArrayInputStream(byte[] buf)
    {
        super(buf);
    }

    @Override
    public void close() throws IOException
    {
        Arrays.fill(this.buf, (byte) 0); // Overwrite buffer on close
        super.close();
    }
}

