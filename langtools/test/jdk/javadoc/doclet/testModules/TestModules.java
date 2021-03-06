/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8154119 8154262
 * @summary Test modules support in javadoc.
 * @author bpatel
 * @library ../lib
 * @modules jdk.javadoc/jdk.javadoc.internal.tool
 * @build JavadocTester
 * @run main TestModules
 */

public class TestModules extends JavadocTester {

    public static void main(String... args) throws Exception {
        TestModules tester = new TestModules();
        tester.runTests();
    }

    @Test
    void test1() {
        javadoc("-d", "out", "-use",
                "-modulesourcepath", testSrc,
                "-addmods", "module1,module2",
                "testpkgmdl1", "testpkgmdl2");
        checkExit(Exit.OK);
        testDescription(true);
        testNoDescription(false);
        testModuleLink();
    }

    @Test
    void test2() {
        javadoc("-d", "out-html5", "-html5", "-use",
                "-modulesourcepath", testSrc,
                "-addmods", "module1,module2",
                "testpkgmdl1", "testpkgmdl2");
        checkExit(Exit.OK);
        testHtml5Description(true);
        testHtml5NoDescription(false);
        testModuleLink();
    }

    @Test
    void test3() {
        javadoc("-d", "out-nocomment", "-nocomment", "-use",
                "-modulesourcepath", testSrc,
                "-addmods", "module1,module2",
                "testpkgmdl1", "testpkgmdl2");
        checkExit(Exit.OK);
        testDescription(false);
        testNoDescription(true);
        testModuleLink();
    }

    @Test
    void test4() {
        javadoc("-d", "out-html5-nocomment", "-nocomment", "-html5", "-use",
                "-modulesourcepath", testSrc,
                "-addmods", "module1,module2",
                "testpkgmdl1", "testpkgmdl2");
        checkExit(Exit.OK);
        testHtml5Description(false);
        testHtml5NoDescription(true);
        testModuleLink();
    }

    @Test
    void test5() {
        javadoc("-d", "out-nomodule", "-use",
                "-sourcepath", testSrc,
                "testpkgnomodule");
        checkExit(Exit.OK);
    }

    void testDescription(boolean found) {
        checkOutput("module1-summary.html", found,
                "<!-- ============ MODULE DESCRIPTION =========== -->\n"
                + "<a name=\"module.description\">\n"
                + "<!--   -->\n"
                + "</a>\n"
                + "<div class=\"block\">This is a test description for the module1 module.</div>");
        checkOutput("module2-summary.html", found,
                "<!-- ============ MODULE DESCRIPTION =========== -->\n"
                + "<a name=\"module.description\">\n"
                + "<!--   -->\n"
                + "</a>\n"
                + "<div class=\"block\">This is a test description for the module2 module.</div>");
    }

    void testNoDescription(boolean found) {
        checkOutput("module1-summary.html", found,
                "<div class=\"contentContainer\">\n"
                + "<ul class=\"blockList\">\n"
                + "<li class=\"blockList\">\n"
                + "<table class=\"overviewSummary\" summary=\"Package Summary table, listing packages, and an explanation\">");
        checkOutput("module2-summary.html", found,
                "<div class=\"contentContainer\">\n"
                + "<ul class=\"blockList\">\n"
                + "<li class=\"blockList\">\n"
                + "<table class=\"overviewSummary\" summary=\"Package Summary table, listing packages, and an explanation\">");
    }

    void testHtml5Description(boolean found) {
        checkOutput("module1-summary.html", found,
                "<section role=\"region\">\n"
                + "<!-- ============ MODULE DESCRIPTION =========== -->\n"
                + "<a id=\"module.description\">\n"
                + "<!--   -->\n"
                + "</a>\n"
                + "<div class=\"block\">This is a test description for the module1 module.</div>\n"
                + "</section>");
        checkOutput("module2-summary.html", found,
                "<section role=\"region\">\n"
                + "<!-- ============ MODULE DESCRIPTION =========== -->\n"
                + "<a id=\"module.description\">\n"
                + "<!--   -->\n"
                + "</a>\n"
                + "<div class=\"block\">This is a test description for the module2 module.</div>\n"
                + "</section>");
    }

    void testHtml5NoDescription(boolean found) {
        checkOutput("module1-summary.html", found,
                "<div class=\"contentContainer\">\n"
                + "<ul class=\"blockList\">\n"
                + "<li class=\"blockList\">\n"
                + "<table class=\"overviewSummary\">");
        checkOutput("module2-summary.html", found,
                "<div class=\"contentContainer\">\n"
                + "<ul class=\"blockList\">\n"
                + "<li class=\"blockList\">\n"
                + "<table class=\"overviewSummary\">");
    }

    void testModuleLink() {
        checkOutput("overview-summary.html", true,
                "<li>Module</li>");
        checkOutput("module1-summary.html", true,
                "<li class=\"navBarCell1Rev\">Module</li>");
        checkOutput("module2-summary.html", true,
                "<li class=\"navBarCell1Rev\">Module</li>");
        checkOutput("testpkgmdl1/package-summary.html", true,
                "<li><a href=\"../module1-summary.html\">Module</a></li>");
        checkOutput("testpkgmdl1/TestClassInModule1.html", true,
                "<li><a href=\"../module1-summary.html\">Module</a></li>");
        checkOutput("testpkgmdl1/class-use/TestClassInModule1.html", true,
                "<li><a href=\"../../module1-summary.html\">Module</a></li>");
        checkOutput("testpkgmdl2/package-summary.html", true,
                "<li><a href=\"../module2-summary.html\">Module</a></li>");
        checkOutput("testpkgmdl2/TestClassInModule2.html", true,
                "<li><a href=\"../module2-summary.html\">Module</a></li>");
        checkOutput("testpkgmdl2/class-use/TestClassInModule2.html", true,
                "<li><a href=\"../../module2-summary.html\">Module</a></li>");
    }

    void testNoModuleLink() {
        checkOutput("testpkgnomodule/package-summary.html", true,
                "<ul class=\"navList\" title=\"Navigation\">\n"
                + "<li><a href=\"../testpkgnomodule/package-summary.html\">Package</a></li>");
        checkOutput("testpkgnomodule/TestClassNoModule.html", true,
                "<ul class=\"navList\" title=\"Navigation\">\n"
                + "<li><a href=\"../testpkgnomodule/package-summary.html\">Package</a></li>");
        checkOutput("testpkgnomodule/class-use/TestClassNoModule.html", true,
                "<ul class=\"navList\" title=\"Navigation\">\n"
                + "<li><a href=\"../../testpkgnomodule/package-summary.html\">Package</a></li>");
    }
}
