# What and why is small D?

 Language Engineering as a Toy Language, name is Small D. D is abbreviation of DSL (Domain Specific Language). 
 DSL Definition: it is not a general-purpose language like a c or java, specialized in particular purpose computer language.
 
 Usually you don't want to implement programming-language for time-limited project and budget-limited system. 
 But sometime you need to do it. many documents you will find but hard to find simple and easy reference. 
 and the specialized purpose language is only using in specific system and it does not open source publicity easily. 
 
 Furthermore for about tricky and cumbersome documents about language implementing and developing IDE.
 Even when you try to find about debugger. suddenly you will encounter linux gdb low level or complex language doc only.
 it make us confused what should i do as a next.
 
 so i made a small programming language and some IDE feature for learn. :)
 i hope this help you.

# RoadMap
 * see org.xtext.labs.mydsl.product\ref\roadmap.png
 
 What: Lexer => Parser => Interpreter => Debuggable Interpreter => Integrate with Eclipse Debug UI 
 Intermediate Product: Token(lexer) => AST(parser) => Call Stack, Symbol table(interpreter)
 How: Xtext Framework & Some implements by myself. 

# Features of small D Project

 1. small DSL language with Xtext
 : Error Checking, Semantic Coloring, Syntax Coloring, Outline, Hover Pop-up, Proposal(Auto-Completion), 
   Scope, Cross-reference, Labeling, MinimapView, Formatting, Quick Fix, Folding, Go To Declaration
 : Xtext official document is not efficient and change some time without proper guidance and very tiny community and reference. 
   but No way. better than nothing. 
 
 2. Debuggable Interpreter & Debugger 
 : Call Stack, Symbol Table, AST(Antlr Parser customized by Xtext.)
 : Interpreter can control interpreting processing like a suspend and resume by command.

 3. Integration with Eclipse Debug UI specification
 : Communication between Debugger and Eclipse Debug UI. 
 : Request Socket <=> Response(data) & Event Socket

 4. Eclipse Product for Deploying
 : Deploying your tool with a new launcher(exe) by equinox configuration.
 : this configuration is equipped in org.xtext.labs.mydsl.product project.
 : Just open DSLDeveloper.product in eclipse and then use Eclipse product export wizard in Overview Tab.
 : if you want to make installer file for your Product. 
 : innosetup is best choice without extra paying. (www.jrsoftware.org/isinfo.php)

 5. Java & C# generator
 : dsl file can be convert Java & C#, not perfectly matching.
 : C# will be convert all function parameter with ref keyword. one of sample for call by reference.
 * especially pascal is one of famous language implemented call by reference.   

# small D specification

  *small D sample code is in "org.xtext.labs.mydsl.product/src"
  
 - num, string, bool (num is int, string, bool is boolean)
 - multi array
 - if ~ else
 - while
 - function definition : "def function_name(){}"
 - launch_main is main function.
 - scope: try to find local variables first. if not, try to search global.

 - no class and object-oriented programming.
 - if expression supported but not support else if. it need to make jump function like a switch case.
 - loop expression supported but while only.
 - we can't use function as a parameter directly. 
    bad case: printstr(numtostr(b)) 
    proper case: a = numtostr(b)
    			 printstr(a)
 - Mydsl.xtext syntax support multiple operation, but interpreter not support yet. sorry for my lazyness.
    syntax point is Mydsl.xtext => "varAssignment returns varExpression:".
    if you want, you can define operator precedence but need to some job.

# Standard function(only for testing)

 - printstr(varStr)
 - strjoin(var1,var2)
 - varArr = strsplit(var1, "delimeter")
 - varStr = numtostr(varNum)
 - getargs(index)
 
# Setup & Deploy

 - Setup
  1. import project in Eclipse.
  2. export org.xtext.labs.mydsl.interpreter as a "Runnable jar" (not just jar) and copy jar into path.
  3. open org.xtext.labs.mydsl.product => DSLDeveloper.product
  4. Launch an Eclipse Product Export Wizard in DSLDeveloper.product's Overview tab
  5. select Project: MyDsl Project => New DSL file
  
 - Deploying Interpreter
 org.xtext.labs.mydsl.interpreter => Export => Runnable jar => debugDSL.jar
 Interpreter is deployed by debugDSL.jar
 Need to Copy a debugDSL.jar in your {DSLDevleoper install path}\debug path.
  ex) D:\DSLDevleoper\debug\debugDSL.jar
 
 - How to set equinox configuration.(from http://www.robertwloch.net)
 1. In the Plug-ins tab change the Launch with select box to plug-ins select below only and uncheck the Target Platform subtree and check the two options below the plug-ins list.
In the search field above the plug-ins list enter equinox. and check the equinox.ds plug-in:
In the filtered list also check the equinox.util plug-in:
Now enter appl in the search field and check the ui.ide.application plug-in:
 2. Switch to the Configuration tab and check Clear the configuration area before launching. This ensures that runtime Eclipse doesnft cache plug-in configuration which avoids occational pitfalls.
 3. The last change needs to be done in the Common tab. Switch the radio choice to Shared file and enter the Project Explorer path to the product plug-in: g/de.rowlo.testgenerator.testdsl.productg. This will tell Eclipse to save that run configuration in a launcher file in the specified location:
 4. Finally switch back to the Plug-ins tab, clear the search filter and check the checkbox Validate plug-ins automatically prior to launching. Then click several times on Add Required Plug-ins right of the plug-ins list. You can stop clicking when the number of selected plug-ins does not change no more. To check if nothingfs missing click on Validate Plug-ins. Eclipse should tell you that no problems were detected. Click on Apply and Close the dialog now.

# References

 1. Xtext
  https://eclipse.org/Xtext/
  https://github.com/LorenzoBettini
  http://www.ne.jp/asahi/hishidama/home/tech/eclipse/xtext/index.html (something is old even all japanese info)
 2. Language Engineering
  https://ruslanspivak.com/lsbasi-part1/
 3. Debugger especially with Eclipse
  https://eclipse.org/articles/Article-Debugger/how-to.html
  http://www.vogella.com/tutorials/EclipseDebugFramework/article.html (best for launchGroup)
  http://codeandme.blogspot.com/ (not many help to me, this site does not open full source code.)
 4. How to Deploy as a Product
  http://www.robertwloch.net/2016/08/the-few-hours-minutes-shiny-dsl-product-tutorial/
 
# Extra Info

 -main function's arguments can be set on "launch configuration view."
 -for debugging, open dsl file with a DSL Editor. not with Xtext Editor.
 -develop under Xtext 2.12.0 and Java SE 1.8. Eclipse Neo.3
 -Minimap View source code can be find in https://github.com/apauzies/eclipse-minimap-view

 -Not a specific case, Sometimes the Variables view remains empty when suspended. 
  Backing View from Screen and taking Front once again. (http://codeandme.blogspot.jp/2013/11/debugger-9-variables-support.html)  
 -Many help from stackoverflow.com.
 
 *debug port
 debug engine use 29777 & 29888 port. if connection exception error occur, close process by process id.
 cmd>>netstat -ona | findstr 0.0:29777
  TCP    0.0.0.0:3000      0.0.0.0:0              LISTENING       3116
 cmd>>taskkill /F /PID 3116
  here 3116 is the process ID
  
# License

 Copyright (c) 2017 Kim iyai@naver.com
 Released under the MIT license. Maybe.

