# smallD
Language Engineering as a Toy Language, The name is Small D. Intergration with Eclipse Debug UI. Simple Interpreter and Debugger.


1. "org.xtext.labs.mydsl.interpreter" part is fully developed and designed based on parsing result, not a feature of Xtend framework. 

2. This interpreter is integrated with eclipse debug UI. This is an independent part from Xtend.

3. The code editor functionalities are created and fixed by Xtend frameworks.

4. The Minimap view and deploy is created by eclipse plugin development.

5. Antlr will parse language definition file in `org.xtext.labs.mydsl/src/org/xtext/labs/Mydsl.xtext`. once it has been done, AST tree has generated, then, this would be fundamental of all customizing in this language engineering. 

### What and why is small D?

Language Engineering as a Toy Language, the name is Small D. D is an abbreviation of DSL (Domain Specific Language). DSL Definition: it is not a general-purpose language like a c or java, specialized in particular purpose computer language.

Usually, you don't want to implement programming-language for a time-limited project and budget-limited system. But sometimes you need to do it. many documents you will find but hard to find the simple and easy reference. and the specialized purpose language is only using in a specific system and it does not open the source to the public easily.

Furthermore for about tricky and cumbersome documents about language implementing and developing IDE, Even when you try to find about the debugger, suddenly you will encounter Linux gdb low level or complex language doc only. so it makes us confused about what should I do as next.

so I made a small programming language and some IDE features for learning. :) I hope this helps you.

### Blueprint
![roadmap](https://user-images.githubusercontent.com/13846660/28236758-70618150-6969-11e7-8913-98eb604a697b.png)
 
 - What: Lexer => Parser => Interpreter => Debuggable Interpreter => Integrate with Eclipse Debug UI
 - Intermediate Product: Token(lexer) => AST(parser) => Call Stack, Symbol table(interpreter)
 - How: Xtext Framework & Some implements by myself. </br>

### Features of small D Project

 1. small DSL language with Xtext
 : Error Checking, Semantic Coloring, Syntax Coloring, Outline, Hover Pop-up, Proposal(Auto-Completion), <
   Scope, Cross-reference, Labeling, MinimapView, Formatting, Quick Fix, Folding, Go To Declaration
 : Xtext official document is not efficient and changes sometimes without proper guidance and very tiny community and reference. 
 but No way. better than nothing. 
 
 
 2. Debuggable Interpreter & Debugger 
 : Call Stack, Symbol Table, AST(Antlr Parser customized by Xtext.)
 : Interpreter can control interpreting processing like a suspend and resume by command.


 3. Integration with Eclipse Debug UI specification
 : Communication between Debugger and Eclipse Debug UI. 
 : Request Socket <=> Response(data) & Event Socket
 
![debugui](https://user-images.githubusercontent.com/13846660/28236759-7064b104-6969-11e7-8896-cbf309b023eb.png)


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

### small D specification

  small D sample code is in "org.xtext.labs.mydsl.product/src"
  
  ![grammar](https://user-images.githubusercontent.com/13846660/28236760-7066258e-6969-11e7-935a-c328ae9dabab.PNG)
  
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
___bad case: printstr(numtostr(b)) 
___proper case: a = numtostr(b)
______________printstr(a)
 - Mydsl.xtext syntax support multiple operation, but interpreter not support yet. sorry for my lazyness.
    syntax point is Mydsl.xtext => "varAssignment returns varExpression:".<br/>
    if you want, you can define operator precedence but need to some job.<br/>

### Standard function(only for testing)

 - printstr(varStr)
 - strjoin(var1,var2)
 - varArr = strsplit(var1, "delimeter")
 - varStr = numtostr(varNum)
 - getargs(index)
 
### Setup & Deploy

 - Setup
  1. import project in Eclipse.
  2. export org.xtext.labs.mydsl.interpreter as a "Runnable jar" (not just jar) and copy jar into path.
  3. open org.xtext.labs.mydsl.product => DSLDeveloper.product
  4. Launch an Eclipse Product Export Wizard in DSLDeveloper.product's Overview tab
  5. select Project: MyDsl Project => New DSL file
  
 - Deploying Interpreter
 org.xtext.labs.mydsl.interpreter => Export => Runnable jar => debugDSL.jar </br>
 Interpreter is deployed by debugDSL.jar </br>
 Need to Copy a debugDSL.jar in your {DSLDevleoper install path}\debug path. </br>
 __ex) D:\DSLDevleoper\debug\debugDSL.jar </br>
 
 - How to set equinox configuration.(from http://www.robertwloch.net)
 1. In the Plug-ins tab change the Launch with select box to plug-ins select below only and uncheck the Target Platform subtree and check the two options below the plug-ins list.
In the search field above the plug-ins list enter equinox. and check the equinox.ds plug-in:
In the filtered list also check the equinox.util plug-in:
Now enter appl in the search field and check the ui.ide.application plug-in:
 2. Switch to the Configuration tab and check Clear the configuration area before launching. This ensures that runtime Eclipse doesn't cache plug-in configuration which avoids occational pitfalls.
 3. The last change needs to be done in the Common tab. Switch the radio choice to Shared file and enter the Project Explorer path to the product plug-in: /de.rowlo.testgenerator.testdsl.product. This will tell Eclipse to save that run configuration in a launcher file in the specified location:
 4. Finally switch back to the Plug-ins tab, clear the search filter and check the checkbox Validate plug-ins automatically prior to launching. Then click several times on Add Required Plug-ins right of the plug-ins list. You can stop clicking when the number of selected plug-ins does not change no more. To check if nothing's missing click on Validate Plug-ins. Eclipse should tell you that no problems were detected. Click on Apply and Close the dialog now.

### References

 1. Xtext<br/>
  https://eclipse.org/Xtext/<br/>
  https://github.com/LorenzoBettini<br/>
  http://www.ne.jp/asahi/hishidama/home/tech/eclipse/xtext/index.html (something is old even all japanese info)<br/>
 2. Language Engineering<br/>
  https://ruslanspivak.com/lsbasi-part1/<br/>
 3. Debugger especially with Eclipse<br/>
  https://eclipse.org/articles/Article-Debugger/how-to.html<br/>
  http://www.vogella.com/tutorials/EclipseDebugFramework/article.html (best for launchGroup)<br/>
  http://codeandme.blogspot.com/ (not many help to me, this site does not open full source code.)<br/>
 4. How to Deploy as a Product<br/>
  http://www.robertwloch.net/2016/08/the-few-hours-minutes-shiny-dsl-product-tutorial/<br/>
 
### Extra Info

 -main function's arguments can be set on "launch configuration view." <br/>
 -for debugging, open dsl file with a DSL Editor. not with Xtext Editor.<br/>
 -develop under Xtext 2.12.0 and Java SE 1.8. Eclipse Neo.3<br/>
 -Minimap View source code can be find in https://github.com/apauzies/eclipse-minimap-view<br/>

 -Not able to replicate a specific case, Sometimes the Variables view widget remains empty when it is suspended. <br/>
  In this case, Switching the View and re-loading the widget once again. <br/>
  (http://codeandme.blogspot.jp/2013/11/debugger-9-variables-support.html) <br/> 
 -Many help from stackoverflow.com.<br/>
 
 - debug port<br/>
 debug engine use 29777 & 29888 port. if connection exception error occur, close process by process id.<br/>
 cmd>>netstat -ona | findstr 0.0:29777<br/>
  TCP    0.0.0.0:3000      0.0.0.0:0              LISTENING       3116<br/>
 cmd>>taskkill /F /PID 3116<br/>
  here 3116 is the process ID<br/>
  
### License

 Copyright (c) 2017 Kim iyai@naver.com<br/>
