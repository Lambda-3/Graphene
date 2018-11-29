package org.lambda3.graphene.cli;

/*-
 * ==========================License-Start=============================
 * VersionTest.java - Graphene CLI - Lambda^3 - 2017
 * Graphene
 * %%
 * Copyright (C) 2017 Lambda^3
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ==========================License-End===============================
 */


import org.testng.Assert;
import org.testng.annotations.Test;

public class VersionTest extends GrapheneCLITest {

	@Test
	public void testVersionInfo() {
		String expected = "Graphene VersionInfo: {\n" +
                "  \"name\": \"Graphene-Core-DEV\",\n" +
                "  \"version\": \"3.1.0\",\n" +
                "  \"buildNumber\":";

		GrapheneCLI.main(new String[]{"--version"});

		String actual = outContent.toString();

		Assert.assertTrue(actual.contains(expected));

		asserted = false;
	}

}
