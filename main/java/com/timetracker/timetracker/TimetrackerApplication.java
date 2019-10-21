package com.timetracker.timetracker;

import com.coxautodev.graphql.tools.SchemaParser;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.UserRepository;
import com.timetracker.timetracker.resolver.MutationResolver;
import com.timetracker.timetracker.resolver.QueryResolver;
import com.timetracker.timetracker.resolver.SubtaskResolver;
import com.timetracker.timetracker.resolver.TaskResolver;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TimetrackerApplication {

    @Autowired
    ClientRepository clientRepo;
    @Autowired
    SubtaskRepository subtaskRepo;
    @Autowired
    TaskRepository taskRepo;
    @Autowired
    UserRepository userRepo;

	public static void main(String[] args) {
		SpringApplication.run(TimetrackerApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {

		GraphQLSchema schema  = SchemaParser.newParser()
				.resolvers(authorResolver(ar), mutation(br, ar), query(br, ar))
				.file("graphql/author.graphqls")
				.file("graphql/book.graphqls")
				.build().makeExecutableSchema();
		ExecutionStrategy executionStrategy = new AsyncExecutionStrategy();
		GraphQLServlet servlet = new SimpleGraphQLServlet(schema, executionStrategy);
		ServletRegistrationBean bean = new ServletRegistrationBean(servlet, "/graphql");
		return bean;
	}



    @Bean
    public TaskResolver taskResolver(ClientRepository clientRepo, SubtaskRepository subtaskRepo) {
        return new TaskResolver(clientRepo, subtaskRepo);
    }

    @Bean
    public SubtaskResolver subtaskResolver(SubtaskRepository subtaskRepo) {
        return new SubtaskResolver(subtaskRepo);
    }

    @Bean
    public QueryResolver queryResolver(TaskRepository taskRepo) {
        return new QueryResolver(taskRepo);
    }

    @Bean
    public MutationResolver mutation(AuthorRepository authorRepository, BookRepository bookRepository) {
        return new Mutation(authorRepository, bookRepository);
    }
}
}
