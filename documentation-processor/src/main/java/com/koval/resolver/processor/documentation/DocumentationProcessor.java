package com.koval.resolver.processor.documentation;

import java.io.File;
import java.io.IOException;

import com.koval.resolver.common.api.bean.issue.Issue;
import com.koval.resolver.common.api.bean.result.IssueAnalysingResult;
import com.koval.resolver.common.api.component.processor.IssueProcessor;
import com.koval.resolver.common.api.configuration.Configuration;
import com.koval.resolver.common.api.doc2vec.TextDataExtractor;
import com.koval.resolver.common.api.doc2vec.VectorModel;
import com.koval.resolver.common.api.doc2vec.VectorModelSerializer;
import com.koval.resolver.processor.documentation.core.DocFileRepository;
import com.koval.resolver.processor.documentation.core.DocOutputFilesParser;
import com.koval.resolver.processor.documentation.core.DocumentationProcessorDelegate;


public class DocumentationProcessor implements IssueProcessor {
  private DocumentationProcessorDelegate mDocumentationProcessorDelegate;

  public DocumentationProcessor(Configuration properties) throws IOException {
    DocFileRepository docFileRepository = new DocFileRepository();
    DocOutputFilesParser docOutputFilesParser = new DocOutputFilesParser(
            properties.getProcessors().getDocumentation(),
            docFileRepository
    );

    VectorModelSerializer vectorModelSerializer = new VectorModelSerializer();
    File vectorModelFile = new File(properties.getProcessors().getDocumentation().getVectorModelFile());
    VectorModel vectorModel = vectorModelSerializer.deserialize(vectorModelFile, properties.getParagraphVectors().getLanguage());
    String docsPath = properties.getProcessors().getDocumentation().getDocsFolder();
    TextDataExtractor textDataExtractor = new TextDataExtractor();

    mDocumentationProcessorDelegate = new DocumentationProcessorDelegate(
            properties,
            docOutputFilesParser,
            vectorModel,
            textDataExtractor,
            docsPath
    );
  }

  @Override
  public void run(Issue issue, IssueAnalysingResult result) {
    mDocumentationProcessorDelegate.run(issue, result);
  }
}
