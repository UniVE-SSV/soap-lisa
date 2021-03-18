package it.unive.lisa.analysis.dataflow.impl;

import it.unive.lisa.analysis.dataflow.DataflowElement;
import it.unive.lisa.analysis.dataflow.DefiniteForwardDataflowDomain;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.NullConstant;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.symbolic.value.TernaryExpression;
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.symbolic.value.ValueExpression;
import java.util.Collection;
import java.util.HashSet;

/**
 * An element of the available expression dataflow domain.
 */
public class AvailableExpressions
		implements DataflowElement<DefiniteForwardDataflowDomain<AvailableExpressions>, AvailableExpressions> {

	/**
	 * The variable defined
	 */
	private final Identifier id;

	/**
	 * The available expression assigned to {@code id}
	 */
	private final ValueExpression expression;

	/**
	 * Builds an instance of {@code AvailableExpression}.
	 */
	public AvailableExpressions() {
		this(null, null);
	}

	private AvailableExpressions(Identifier id, ValueExpression expression) {
		this.id = id;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "(" + id + ", " + expression + ")";
	}

	@Override
	public Identifier getIdentifier() {
		return id;
	}

	private Collection<Identifier> getIdentifierOperands(SymbolicExpression expression) {
		Collection<Identifier> result = new HashSet<>();

		if (expression instanceof Identifier)
			result.add((Identifier) expression);

		if (expression instanceof UnaryExpression)
			result.addAll(getIdentifierOperands(((UnaryExpression) expression).getExpression()));

		if (expression instanceof BinaryExpression) {
			BinaryExpression binary = (BinaryExpression) expression;
			result.addAll(getIdentifierOperands(binary.getLeft()));
			result.addAll(getIdentifierOperands(binary.getRight()));
		}

		if (expression instanceof TernaryExpression) {
			TernaryExpression ternary = (TernaryExpression) expression;
			result.addAll(getIdentifierOperands(ternary.getLeft()));
			result.addAll(getIdentifierOperands(ternary.getMiddle()));
			result.addAll(getIdentifierOperands(ternary.getRight()));
		}

		return result;
	}

	private boolean containsId(SymbolicExpression exp, Identifier id) {
		if (exp instanceof Identifier)
			return id.equals(exp);

		if (exp instanceof Constant || exp instanceof NullConstant)
			return false;

		if (exp instanceof UnaryExpression)
			return containsId(((UnaryExpression) exp).getExpression(), id);

		if (exp instanceof BinaryExpression)
			return containsId(((BinaryExpression) exp).getLeft(), id)
					|| containsId(((BinaryExpression) exp).getRight(), id);

		if (exp instanceof TernaryExpression)
			return containsId(((TernaryExpression) exp).getLeft(), id)
					|| containsId(((TernaryExpression) exp).getRight(), id)
					|| containsId(((TernaryExpression) exp).getMiddle(), id);

		return false;
	}

	@Override
	public Collection<AvailableExpressions> gen(Identifier id, ValueExpression expression, ProgramPoint pp,
			DefiniteForwardDataflowDomain<AvailableExpressions> domain) {
		Collection<AvailableExpressions> result = new HashSet<>();
		if (!(expression instanceof PushAny) && !(containsId(expression, id)))
			result.add(new AvailableExpressions(id, expression));
		return result;
	}

	@Override
	public Collection<Identifier> kill(Identifier id, ValueExpression expression, ProgramPoint pp,
			DefiniteForwardDataflowDomain<AvailableExpressions> domain) {
		Collection<Identifier> result = new HashSet<>();
		result.add(id);

		for (AvailableExpressions ae : domain.getDataflowElements()) {
			Collection<Identifier> ids = getIdentifierOperands(ae.expression);

			if (ids.contains(id))
				result.add(ae.getIdentifier());
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvailableExpressions other = (AvailableExpressions) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}