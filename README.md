## Скачиваем исходные данные - 1000 Genomes Phase 3, пока только хромосома 22
``` bash
aria2c ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz.tbi
```

## Считаем количество образцов — получается 2504
``` bash
tabix --only-header ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz | grep CHROM | tr '\t' '\n' | tail -n+10 | wc -l
```

## Оцениваем количество мутаций на 22 хромосоме — получается 1103547
``` bash
bcftools view --no-header ALL.chr22.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz | wc -l
```

## Получаем BED-файл со списком всех экзонов

### Устанавливаем bedops
``` bash
wget https://github.com/bedops/bedops/releases/download/v2.4.30/bedops_linux_x86_64-v2.4.30.tar.bz2
tar --extract --file=bedops_linux_x86_64-v2.4.30.tar.bz2
mv bin/* /usr/bin/
```

### Формируем список экзонов
``` bash
wget -q 'ftp://ftp.ensembl.org/pub/grch37/release-91/gtf/homo_sapiens/Homo_sapiens.GRCh37.87.gtf.gz'
gzip -d Homo_sapiens.GRCh37.87.gtf.gz
awk '{ if ($0 ~ "transcript_id") print $0; else print $0" transcript_id \"\";"; }' Homo_sapiens.GRCh37.87.gtf | gtf2bed - > annotation.bed
grep 'gene_biotype "protein_coding"' annotation.bed | awk '{print "chr" $0}' | sed -e 's|chrMT|chrM|g' | bedtools sort >coding.bed
awk '{if ($8 == "exon") print $0}' coding.bed >exons.bed
sed -e 's|^chr||' exons.bed | cut -f-3 > exons.filter.bed
```

### Фильтруем VCF-ку
```bash
bcftools intersect -A <...>.vcf -B exons.filter.bed > output.vcf
```
bcftools query -f'%CHROM %POS %REF %ALT %INFO/AF [%GT ]\n'  -i'AF>0.005 && AF<0.05' file1.vcf > file1.txt
