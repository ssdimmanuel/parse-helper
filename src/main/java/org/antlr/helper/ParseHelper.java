package org.antlr.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class ParseHelper {

	private Class<? extends Parser> parserClass;
	private Class<? extends Lexer> lexerClass;
	private Class<? extends ParseTreeListener> listenerClass;
	private String topRule;
	
	public ParseHelper(Class<? extends Parser> parserClass, Class<? extends Lexer> lexerClass, Class<? extends ParseTreeListener> listenerClass,
			String topRule) {
		super();
		this.parserClass = parserClass;
		this.lexerClass = lexerClass;
		this.listenerClass = listenerClass;
		this.topRule = topRule;
	}

	public void parseInput(String input) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CharStream cs = CharStreams.fromString(input);
		Lexer lex = lexerClass.getConstructor(CharStream.class).newInstance(cs);
		CommonTokenStream ct = new CommonTokenStream(lex);
		
		Parser p = parserClass.getConstructor(TokenStream.class).newInstance(ct);
		
		Method parsemethod = p.getClass().getDeclaredMethod(topRule);
		
		ParserRuleContext ctx = (ParserRuleContext) parsemethod.invoke(p);
		
		ParseTreeListener ptl = listenerClass.getConstructor().newInstance();
		
		ParseTreeWalker.DEFAULT.walk(ptl, ctx);
	}

	
	

}
