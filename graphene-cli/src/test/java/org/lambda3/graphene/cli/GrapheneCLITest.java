/*
 * ==========================License-Start=============================
 * graphene-cli : GrapheneCLITest
 *
 * Copyright © 2017 Lambda³
 *
 * GNU General Public License 3
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * ==========================License-End==============================
 */

package org.lambda3.graphene.cli;

import com.typesafe.config.ConfigFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrapheneCLITest {

	protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	protected final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	protected boolean asserted;
	protected PrintStream originalOut;
	protected PrintStream originalErr;

	@BeforeTest
	void setup() {

		originalOut = System.out;
		originalErr = System.err;

		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		asserted = true;

		System.setProperty("config.resource", "application-cli.local.conf");
		ConfigFactory.invalidateCaches();
	}


	@AfterTest
	void printIfError() {
		if (asserted) {
			System.setOut(originalOut);
			System.setErr(originalErr);

			System.out.println(outContent.toString());
			System.err.println(errContent.toString());
		}
	}

}
