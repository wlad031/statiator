package dev.vgerasimov.example;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.InvalidArgumentException;
import com.hubspot.jinjava.interpret.InvalidInputException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.RenderResult;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MyJinjava extends Jinjava {

    /**
     * Create a new Jinjava processor instance with the default global config
     */
    public MyJinjava(Context globalContext) {
        super();
        setGlobalContext(globalContext);
    }

    public void setGlobalContext(Context globalContext) {
        try {
            var field = this.getClass().getSuperclass().getDeclaredField("globalContext");
            field.setAccessible(true);
            field.set(this, globalContext);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Cannot set global context", e);
        }
    }

    public Context copyGlobalContext() {
        try {
            var copyGlobalContext = this.getClass().getSuperclass().getDeclaredMethod("copyGlobalContext");
            copyGlobalContext.setAccessible(true);
            return (Context) copyGlobalContext.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Cannot copy global context", e);
        }
    }

    /**
     * Render the given template using the given context bindings. This method returns some metadata about the render process, including any errors which may have been encountered such as unknown variables or syntax errors. This method will
     * not throw any exceptions; it is up to the caller to inspect the renderResult.errors collection if necessary / desired.
     *
     * @param template     jinja source template
     * @param bindings     map of objects to put into scope for this rendering action
     * @param renderConfig used to override specific config values for this render operation
     * @return result object containing rendered output, render context, and any encountered errors
     */
    public RenderResult renderForResult(
            String template,
            Map<String, ?> bindings,
            JinjavaConfig renderConfig
    ) {
        Context context;
        var parentInterpreter = JinjavaInterpreter.getCurrent();
        if (parentInterpreter != null) {
            renderConfig = parentInterpreter.getConfig();
            var bindingsWithParentContext = new HashMap<String, Object>(bindings);
            if (parentInterpreter.getContext() != null) {
                bindingsWithParentContext = new HashMap<>(parentInterpreter.getContext());
                bindingsWithParentContext.putAll(bindings);
            }
            context =
                    new Context(
                            copyGlobalContext(),
                            bindingsWithParentContext,
                            renderConfig.getDisabled()
                    );
        } else {
            context = new Context(copyGlobalContext(), bindings, renderConfig.getDisabled());
        }

        var interpreter = getGlobalConfig()
                .getInterpreterFactory()
                .newInstance(this, context, renderConfig);
        JinjavaInterpreter.pushCurrent(interpreter);

        try {
            return new RenderResult(
                    interpreter.render(template),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } catch (TemplateSyntaxException e) {
            return new RenderResult(
                    TemplateError.fromException(e),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } catch (InterpretException e) {
            return new RenderResult(
                    TemplateError.fromSyntaxError(e),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } catch (InvalidArgumentException e) {
            return new RenderResult(
                    TemplateError.fromInvalidArgumentException(e),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } catch (InvalidInputException e) {
            return new RenderResult(
                    TemplateError.fromInvalidInputException(e),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } catch (Exception e) {
            return new RenderResult(
                    TemplateError.fromException(e),
                    interpreter.getContext(),
                    interpreter.getErrorsCopy()
            );
        } finally {
            getGlobalContext().reset();
            JinjavaInterpreter.popCurrent();
        }
    }
}
