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
package pt.com.gmv.lab.implant.rendering.marker.wavefront;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.com.gmv.lab.implant.exceptions.ModelLoadingException;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.ModelLoader;
import pt.com.gmv.lab.implant.rendering.marker.wavefront.parts.Face;

/**
 * A loader for Wavefront OBJ models.
 * 
 * @author fao
 * @since 1
 * 
 * @see http://www.eg-models.de/formats/Format_Obj.html
 */
public final class ObjLoader implements ModelLoader {

	/**
	 * Loads a model from a given location defined by a simple subset of the Wavefront OBJ
	 * specification.
	 * 
	 * @return A loaded model representing the model defined by the file.
	 * 
	 * @throws ModelLoadingException In case there's a problem reading the file.
	 */
	@Override
	public final Model load(final String location) throws ModelLoadingException {
		String[] splitLine;
		String line;
		final File file;
		final BufferedReader bs;

		if(!(file = new File(location)).exists())
			throw new ModelLoadingException(location);

		final float[] vCoords = new float[3];
		final float[] tCoords = new float[2];
		final float[] nCoords = new float[3];
		final ObjModel model = new ObjModel();

		try {
			bs = new BufferedReader(new InputStreamReader(new FileInputStream(file)));


			while((line = bs.readLine()) != null) {
				line = line.trim();

				if((line.length() == 0) || (line.charAt(0) == '#'))
					continue;

				if(line.charAt(0) == 'v' && line.charAt(1) == ' ') {
					splitLine = line.split("\\s+");

					for(int i = 1; i < splitLine.length; ++i)
						vCoords[i - 1] = Float.parseFloat(splitLine[i]);

					model.addVertex(vCoords[0], vCoords[1], vCoords[2]);

				} else if(line.charAt(0) == 'v' && line.charAt(1) == 't') {
					splitLine = line.split("\\s+");
					tCoords[0] = Float.valueOf(splitLine[1]).floatValue();
					tCoords[1] = Float.valueOf(splitLine[2]).floatValue();

				} else if(line.charAt(0) == 'v' && line.charAt(1) == 'n') {
					splitLine = line.split("\\s+");
					nCoords[0] = Float.valueOf(splitLine[1]).floatValue();
					nCoords[1] = Float.valueOf(splitLine[2]).floatValue();

				} else if(line.charAt(0) == 'f' && line.charAt(1) == ' ') {
					splitLine = line.split("\\s+");
					int[] v = new int[splitLine.length - 1];

					for (int i=1; i < splitLine.length; i++) {
						String fixstring = splitLine[i].replaceAll("//","/0/");
						String[] tempstring = fixstring.split("/");
						v[i-1] = Integer.valueOf(tempstring[0]).intValue();
					}

					if (v.length == 3)
						model.addFace(new Face(model.getVertex(v[0]-1), model.getVertex(v[1]-1), model.getVertex(v[2]-1)));
					else if (v.length == 4)
						model.addFace(new Face(model.getVertex(v[0]-1), model.getVertex(v[1]-1), model.getVertex(v[2]-1), model.getVertex(v[3]-1)));

				}
			}
		} catch (FileNotFoundException e) {
			throw new ModelLoadingException(location, e);
		} catch (NumberFormatException e) {
			throw new ModelLoadingException(location, e);
		} catch (IOException e) {
			throw new ModelLoadingException(location, e);
		}

		return model;
	}
}
