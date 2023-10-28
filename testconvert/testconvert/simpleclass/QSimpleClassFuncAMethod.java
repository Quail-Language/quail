package testconvert.simpleclass;

class QSimpleClassFuncAMethod extends QBuiltinFunc { 

	public QSimpleClassFuncAMethod(
		Runtime runtime,
		Memory closure
	) {
		super( 
		        "aMethod", 
		         Arrays.asList( 
		new FuncArgument(
		        "arg0",
		        QObject.Val(),
		       new int[] {ModifierConstants.STR},
		        LiteralFunction.Argument.POSITIONAL
		)
	
		         ), 
		         runtime, 
		         closure, 
		         false 
		); 
	}
	
	public QSimpleClassFuncAMethod(
		Runtime runtime
	) {
		this(runtime, runtime.getMemory()); 
	} 

	public QObject action(
		Runtime runtime,
		<HashMap extends String & QObject> args,
		<List extends QObject> argList
	) throws RuntimeStriker {
		if (!(args.get("this") instanceof QSimpleClass)) 
		   runtime.error(new QUnsuitableTypeException("QSimpleClass", args.get("this"))); 
		QSimpleClass thisObject = ((QSimpleClass) args.get("this")); 
		if (!thisObject.isInitialized()) 
		   runtime.error(new QNotInitializedException("QSimpleClass")); 
		java.lang.String argArg0; 
		argArg0 = Objects.requireNonNull(arg0.strValue());
	
		thisObject.aMethod(argArg0);
	
	} 

}