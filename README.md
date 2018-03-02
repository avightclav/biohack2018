# Скачиваем исходные данные - 1000 Genomes Phase 3, пока только хромосома 22
aria2c ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz.tbi 

# Считаем количество образцов (должно получиться 2504)
tabix --only-header ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz | grep CHROM | tr '\t' '\n' | tail -n+10 | wc -l
