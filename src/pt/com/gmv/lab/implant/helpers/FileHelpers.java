/**
 * Copyright (C) 2010 Fábio Oliveira.
 * All rights reserved.
 *
 * This file is part of the IMPLANT project.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Fábio Oliveira nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package pt.com.gmv.lab.implant.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;

/**
 * An helper class for handling files from the application.
 *
 * @author fao
 * @since 1
 */
/*
 * Why god, oh why, doesn't Java has extensions methods?!
 */
public final class FileHelpers {
	/**
	 * Moves a file in the application container to the device filesystem.
	 * 
	 * @param folder The folder to copy the file to.
	 * @param asset The filename for the file to move.
	 * @param am The asset manager to select the file to move.
	 * 
	 * @throws FileNotFoundException In case the file is not found.
	 * @throws IOException In case the copy operation isn't successful.
	 */
	public final static String moveFileToApplicationFilesystem(final File folder, 
																final String asset, 
																final AssetManager am) 
	throws FileNotFoundException, IOException {		
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		if (folder.exists()) {
			File file = new File(folder, asset);
			
			if (!file.exists()) {
				copy(am.open(asset), new FileOutputStream(file));
			}
			
			return file.getCanonicalPath();
		}
		
		return null; 
	}

	/**
	 * Makes a buffered copy of data.
	 * 
	 * @param in The input stream.
	 * @param out The output stream.
	 * 
	 * @throws IOException In case there's a problem when writing to the output stream.
	 */
	private final static void copy(final InputStream in, final OutputStream out) throws IOException {
	    byte[] buffer = new byte[0xFFFF]; 
	    for (int len; (len = in.read(buffer)) != -1;) 
	      out.write( buffer, 0, len ); 
	}
}
