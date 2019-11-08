<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="Container">
	<table class="table table-striped">
		<caption>Your Todos list</caption>
		<thead>
			<th>Description</th>
			<th>Target date</th>
			<th>Is it done?</th>
			<th></th>
			<th></th>

		</thead>
		<tbody>
			<c:forEach items="${todos}" var="item">
				<tr>
					<td>${item.desc}</td>
					<td><fmt:formatDate value="${item.targetDate}"
							pattern="dd/MM/yyyy" /></td>
					<td>${item.done}</td>
					<td><a class="btn btn-success"
						href="/update-todo?id=${item.id}">Update</a></td>
					<td><a class="btn btn-warning"
						href="/delete-todo?id=${item.id}">Delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div>
		<a class="button" href="/add-todo">Add a Todo</a>
	</div>
</div>
<%@ include file="common/footer.jspf"%>