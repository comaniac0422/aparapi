/*
Copyright (c) 2010-2011, Advanced Micro Devices, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following
disclaimer. 

Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
disclaimer in the documentation and/or other materials provided with the distribution. 

Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

If you use the software (in whole or in part), you shall adhere to all applicable U.S., European, and other export
laws, including but not limited to the U.S. Export Administration Regulations ("EAR"), (15 C.F.R. Sections 730 through
774), and E.U. Council Regulation (EC) No 1334/2000 of 22 June 2000.  Further, pursuant to Section 740.6 of the EAR,
you hereby certify that, except pursuant to a license granted by the United States Department of Commerce Bureau of 
Industry and Security or as otherwise permitted pursuant to a License Exception under the U.S. Export Administration 
Regulations ("EAR"), you will not (1) export, re-export or release to a national of a country in Country Groups D:1,
E:1 or E:2 any restricted technology, software, or source code you receive hereunder, or (2) export to Country Groups
D:1, E:1 or E:2 the direct product of such technology or software, if such foreign produced direct product is subject
to national security controls as identified on the Commerce Control List (currently found in Supplement 1 to Part 774
of EAR).  For the most current Country Group listings, or for additional information about the EAR or your obligations
under those regulations, please refer to the U.S. Bureau of Industry and Security's website at http://www.bis.doc.gov/. 

*/

import com.amd.aparapi.Kernel;
import com.amd.aparapi.Range;
import java.lang.reflect.*;

public class Main{

   public static void main(String[] _args) {

			final int LENGTH = 256;
      final int size = LENGTH * LENGTH;

      final float[] a = new float[size];
      final float[] b = new float[size];

      for (int i = 0; i < size; i++) {
         a[i] = (float) (Math.random() * 10);
         b[i] = (float) (Math.random() * 10);
      }

      final float[] result = new float[size];

      Kernel mul_acc = new Kernel(){
         @Override public void run() {
            int r = getGlobalId(0);
						int c = getGlobalId(1);
						int rank = getGlobalSize(0);
						float running = 0;

						for(int idx = 0; idx < rank; idx++) {
							int aIdx = r * rank + idx;
							int bIdx = idx * rank + c;
							running += a[aIdx] * b[bIdx];
						}
						result[r * rank + c] = running;
         }
      };

			long st = System.currentTimeMillis();
      mul_acc.execute(Range.create2D(LENGTH, LENGTH));
			long et = System.currentTimeMillis() - st;

			System.out.println("done. " + et / 1000.0 + "s");

			int correct = 0;
      for (int i = 0; i < LENGTH; i++) {
				for (int j = 0; j < LENGTH; j++) {
					float running = 0;
					for (int k = 0; k < LENGTH; k++)
						running += a[i * LENGTH + k] * b[k * LENGTH + j];
					if(Math.abs(result[i * LENGTH + j] - running) < 0.01)
						correct++;
//					else
//						System.out.println("Inconsist result: " + result[i * LENGTH + j] + " != " + running);
				}
      }
			System.out.println("Compare: " + correct + "/" + size + " corrects!");

      mul_acc.dispose();
   }

}
